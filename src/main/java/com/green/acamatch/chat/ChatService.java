package com.green.acamatch.chat;

import com.green.acamatch.academy.AcademyRepository;
import com.green.acamatch.chat.model.*;
import com.green.acamatch.config.exception.CommonErrorCode;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.security.AuthenticationFacade;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.academy.Chat;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.user.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserUtils userUtils;
    private final AcademyRepository academyRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatUserRes getChatUserList(ChatRoomGetReq req) {
        if (req == null || (req.getUserId() != null && req.getAcaId() != null)) {
            throw new CustomException(CommonErrorCode.INVALID_PARAMETER);
        }
        Pageable pageable = PageRequest.of(req.getPage() - 1, req.getSize());
        Page<ChatUserList> res = null;
        if (req.getUserId() != null) {
            res = chatRepository.findChatRoomByUserId(req.getUserId(), pageable);
        }
        if (req.getAcaId() != null) {
            res = chatRepository.findChatRoomByAcaId(req.getAcaId(), pageable);
        }

        if (res == null) throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);

        return ChatUserRes.builder()
                .users(res.getContent())
                .totalElements(res.getNumberOfElements())
                .totalPages(res.getTotalPages())
                .build();
    }

    @Transactional
    public List<ChatLogList> getChatLog(ChatLogGetReq req) {
        User user = userUtils.findUserById(req.getUserId());
        Academy academy = academyRepository.findById(req.getAcaId())
                .orElseThrow(() -> new CustomException(CommonErrorCode.INVALID_PARAMETER));
        Pageable pageable = PageRequest.of(req.getPage() - 1, req.getSize());

        List<Chat> chats = chatRepository.findAllByUserAndAcademyOrderByCreatedAtDesc(user, academy, pageable);

        int userType = req.getUserId().equals(AuthenticationFacade.getSignedUserId()) ? 1 : 0;

        // 읽지 않은 메시지에서 isRead 상태만 업데이트
        List<Chat> updatedChats = chats.stream()
                .filter(chat -> !chat.isRead() && chat.getSenderType() == userType)  // 읽지 않은 메시지
                .peek(chat -> chat.setRead(true))  // isRead를 1로 설정
                .collect(Collectors.toList());

        if (!updatedChats.isEmpty()) {
            chatRepository.saveAll(updatedChats);  // 변경된 메시지만 DB에 저장
        }

        return chats.stream()
                .map(ChatLogList::new)
                .collect(Collectors.toList());
    }


    public Integer checkUnreadMessage() {
        User user = userUtils.findUserById(AuthenticationFacade.getSignedUserId());
        if (user.getRole().getRoleId() < 3) {
            return chatRepository.countChatByUserAndSenderTypeAndIsRead(user, 1, false);
        }
        if (user.getRole().getRoleId() == 3) {
            List<Academy> academyList = academyRepository.findAllByUser(user);
            return chatRepository.countChatByAcademyInAndSenderTypeAndIsRead(academyList, 0,false);
        }
        return 0;
    }

    public void saveMessage(ChatSendReq req) {
        Chat chat = new Chat();
        chat.setSenderType(req.getSenderType());
        chat.setMessage(req.getMessage());
        chat.setUser(userUtils.findUserById(req.getUserId()));
        chat.setAcademy(academyRepository.findById(req.getAcaId()).orElseThrow(() -> new CustomException(CommonErrorCode.INVALID_PARAMETER)));
        chatRepository.save(chat);

        messagingTemplate.convertAndSend("/queue/" + req.getUserId() + "_" + req.getAcaId(), req);
    }
}

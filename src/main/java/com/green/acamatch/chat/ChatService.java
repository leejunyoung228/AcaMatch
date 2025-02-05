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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final NotificationController notificationController;
    private final AcademyRepository academyRepository;
    private final UserUtils userUtils;

    public void sendMessage(ChatSendReq req) {
        Chat chat = new Chat();
        Academy academy = academyRepository.findById(req.getAcaId()).orElseThrow(() -> new CustomException(CommonErrorCode.INVALID_PARAMETER));
        User user = userUtils.findUserById(req.getUserId());
        chat.setAcademy(academy);
        chat.setUser(user);
        chat.setMessage(req.getMessage());
        chat.setSenderType(req.getSenderType());
        chatRepository.save(chat);
        switch (SenderType.fromValue(req.getSenderType())) {
            case ACADEMY_TO_USER:
                notificationController.sendNotification(user.getUserId(), "메세지가 도착했습니다");
                break;
            case USER_TO_ACADEMY:
                notificationController.sendNotification(academy.getUser().getUserId(), "메세지가 도착했습니다");
                break;
            default:
                throw new CustomException(CommonErrorCode.INVALID_PARAMETER);
        }
    }

    @Transactional
    public List<ChatLogList> getQna(ChatReq req) {
        User user = userUtils.findUserById(req.getUserId());
        Academy academy = academyRepository.findById(req.getAcaId())
                .orElseThrow(() -> new CustomException(CommonErrorCode.INVALID_PARAMETER));
        Pageable pageable = PageRequest.of(req.getPage() - 1, req.getSize());

        List<Chat> chats = chatRepository.findAllByUserAndAcademyOrderByCreatedAtDesc(user, academy, pageable);

        int userType = req.getUserId().equals(AuthenticationFacade.getSignedUserId()) ? 1 : 0;

        // 읽지 않은 메시지에서 isRead 상태만 업데이트
        List<Chat> updatedChats = chats.stream()
                .filter(chat -> chat.getIsRead() == 0 && chat.getSenderType() == userType)  // 읽지 않은 메시지
                .peek(chat -> chat.setIsRead(1))  // isRead를 1로 설정
                .collect(Collectors.toList());

        if (!updatedChats.isEmpty()) {
            chatRepository.saveAll(updatedChats);  // 변경된 메시지만 DB에 저장
        }

        return chats.stream()
                .map(ChatLogList::new)
                .collect(Collectors.toList());
    }

    public ChatUserRes getUserList(ChatReq req) {
        Page<ChatUserList> res;
        Pageable pageable = PageRequest.of(req.getPage() - 1, req.getSize());
        if (req.getUserId() != null) {
            User user = userUtils.findUserById(req.getUserId());
            res = chatRepository.findByUser(user, pageable);
        } else if (req.getAcaId() != null) {
            Academy academy = academyRepository.findById(req.getAcaId())
                    .orElseThrow(() -> new CustomException(CommonErrorCode.INVALID_PARAMETER));
            res = chatRepository.findByAcademy(academy, pageable);
        } else {
            return ChatUserRes.builder().totalPages(0).users(new ArrayList<>()).build();
        }
        return ChatUserRes.builder()
                .totalPages(res.getTotalPages())
                .totalElements(res.getNumberOfElements())
                .users(res.getContent())
                .build();
    }
}

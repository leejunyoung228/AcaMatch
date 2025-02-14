package com.green.acamatch.chat;

import com.green.acamatch.academy.AcademyRepository;
import com.green.acamatch.chat.model.*;
import com.green.acamatch.config.exception.CommonErrorCode;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.entity.academy.ChatRoom;
import com.green.acamatch.config.security.AuthenticationFacade;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.academy.Chat;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.user.UserUtils;
import com.green.acamatch.entity.myenum.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final AcademyRepository academyRepository;
    private final UserUtils userUtils;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatUserRes getChatUserList(ChatRoomGetReq req) {
        if (req == null || (req.getUserId() != null && req.getAcaId() != null)) {
            throw new CustomException(CommonErrorCode.INVALID_PARAMETER);
        }
        Pageable pageable = PageRequest.of(req.getPage() - 1, req.getSize());
        Page<ChatRoomDto> res = chatRoomRepository.getChatRooms(req, pageable);

        return ChatUserRes.builder()
                .users(res.getContent())
                .totalElements(res.getNumberOfElements())
                .totalPages(res.getTotalPages())
                .build();
    }

    public Integer checkUnreadMessage() {
        User user = userUtils.findUserById(AuthenticationFacade.getSignedUserId());
        if (user.getUserRole().equals(UserRole.STUDENT) || user.getUserRole().equals(UserRole.PARENT)) {
//            return chatRepository.countChatByUserAndSenderTypeAndIsRead(user, SenderType.ACADEMY_TO_USER, false);
        }
        if (user.getUserRole().equals(UserRole.ACADEMY)) {
            List<Academy> academyList = academyRepository.findAllByUser(user);
//            return chatRepository.countChatByAcademyInAndSenderTypeAndIsRead(academyList, SenderType.USER_TO_ACADEMY,false);
        }
        return 0;
    }

    public void saveMessage(ChatSendReq req) {

        ChatRoom chatRoom = chatRoomRepository.findById(req.getChatRoomId())
                .orElseThrow(() -> new CustomException(CommonErrorCode.INVALID_PARAMETER));
        Chat chat = new Chat();
        chat.setChatRoom(chatRoom);
        chat.setSenderType(req.getSenderType());
        chat.setMessage(req.getMessage());
        chatRepository.save(chat);

        messagingTemplate.convertAndSend("/queue/" + chatRoom.getChatRoomId(), req);
    }

    public long getChatRoomId(GetChatRoomIdReq req) {
        User user = userUtils.findUserById(req.getUserId());
        Academy academy = academyRepository.findById(req.getAcaId()).orElseThrow(() -> new CustomException(CommonErrorCode.INVALID_PARAMETER));
        ChatRoom chatRoom = chatRoomRepository.findByUserAndAcademy(user, academy)
                .orElse(null);
        if (chatRoom == null) {
            chatRoom = new ChatRoom();
            chatRoom.setUser(user);
            chatRoom.setAcademy(academy);
            chatRoomRepository.save(chatRoom);
        }
        return chatRoom.getChatRoomId();
    }
}

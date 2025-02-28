package com.green.acamatch.chat;

import com.green.acamatch.academy.AcademyRepository;
import com.green.acamatch.chat.model.*;
import com.green.acamatch.config.exception.CommonErrorCode;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.entity.academy.ChatRoom;
import com.green.acamatch.config.security.AuthenticationFacade;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.academy.Chat;
import com.green.acamatch.entity.myenum.SenderType;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.user.UserUtils;
import com.green.acamatch.entity.myenum.UserRole;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private final EntityManager em;
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
            return chatRepository.countByChatRoom_User_UserIdAndSenderTypeAndIsRead(user.getUserId(), SenderType.ACADEMY_TO_USER, false);
        }
        if (user.getUserRole().equals(UserRole.ACADEMY)) {
            return chatRepository.countByChatRoom_Academy_User_UserIdAndSenderTypeAndIsRead(user.getUserId(), SenderType.USER_TO_ACADEMY, false);
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
        readMessage(req.getChatRoomId(),  req.getSenderType());
        long userId;
        if(req.getSenderType().equals(SenderType.USER_TO_ACADEMY)) {
            userId = chatRoom.getAcademy().getUser().getUserId();
        }else {
            userId = chatRoom.getUser().getUserId();
        }
        messagingTemplate.convertAndSend("/user-alert/" + userId, "메세지가 도착했습니다");
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

    public List<ChatLogList> getChatLog(Long chatRoomId) {
        List<Chat> chats = chatRepository.findByChatRoom_ChatRoomId(chatRoomId);
        if (!chats.isEmpty()) {
            List<Chat> updatedChats = new ArrayList<>();
            SenderType senderType;
            if (chats.get(0).getChatRoom().getUser().getUserId().equals(AuthenticationFacade.getSignedUserId())) {
                senderType = SenderType.ACADEMY_TO_USER;
            } else {
                senderType = SenderType.USER_TO_ACADEMY;
            }
            chats.stream()
                    .filter(chat -> !chat.isRead() && chat.getSenderType().equals(senderType))
                    .forEach(chat -> {
                        chat.setRead(true);
                        updatedChats.add(chat);  // 업데이트된 객체만 저장
                    });
            chatRepository.saveAll(updatedChats);
            return chats.stream().map(ChatLogList::new).toList();
        }
        return new ArrayList<>();
    }

    public void readMessage(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new CustomException(CommonErrorCode.INVALID_PARAMETER));
        SenderType senderType;
        if (chatRoom.getUser().getUserId().equals(AuthenticationFacade.getSignedUserId())) {
            senderType = SenderType.ACADEMY_TO_USER;
        } else {
            senderType = SenderType.USER_TO_ACADEMY;
        }
        List<Chat> unreadChats = chatRepository.findByChatRoom_ChatRoomIdAndSenderTypeAndIsRead(chatRoomId, senderType, false);
        if (!unreadChats.isEmpty()) {
            for (Chat chat : unreadChats) {
                chat.setRead(true);
            }
            chatRepository.saveAll(unreadChats);
        }
    }
    public void readMessage(Long chatRoomId, SenderType senderType) {
        if(senderType.equals(SenderType.ACADEMY_TO_USER)) {
            senderType = SenderType.USER_TO_ACADEMY;
        } else if(senderType.equals(SenderType.USER_TO_ACADEMY)) {
            senderType = SenderType.ACADEMY_TO_USER;
        }
        List<Chat> unreadChats = chatRepository.findByChatRoom_ChatRoomIdAndSenderTypeAndIsRead(chatRoomId, senderType, false);
        if (!unreadChats.isEmpty()) {
            for (Chat chat : unreadChats) {
                chat.setRead(true);
            }
            chatRepository.saveAll(unreadChats);
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteChatLogs() {
        LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);

        int batchSize = 1000; // 한 번에 삭제할 개수
        int deletedRows;

        do {
            deletedRows = chatRepository.bulkDeleteOldChatsWithLimit(sixMonthsAgo, batchSize);
            log.info("Deleted {} old chat records", deletedRows);
            em.flush();
            em.clear();
        } while (deletedRows > 0); // 더 이상 삭제할 데이터가 없을 때까지 반복

        chatRoomRepository.deleteAllByChatsIsEmpty();
    }
}

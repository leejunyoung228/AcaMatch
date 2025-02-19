package com.green.acamatch.chat;

import com.green.acamatch.entity.academy.Chat;
import com.green.acamatch.entity.myenum.SenderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByChatRoom_ChatRoomId(Long chatRoomChatRoomId);

    Integer countByChatRoom_User_UserIdAndSenderTypeAndIsRead(Long chatRoomUserUserId, SenderType senderType, boolean read);

    Integer countByChatRoom_Academy_User_UserIdAndSenderTypeAndIsRead(Long chatRoomAcademyUserUserId, SenderType senderType, boolean read);
}

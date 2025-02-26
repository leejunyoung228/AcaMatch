package com.green.acamatch.chat;

import com.green.acamatch.entity.academy.Chat;
import com.green.acamatch.entity.myenum.SenderType;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByChatRoom_ChatRoomId(Long chatRoomChatRoomId);

    Integer countByChatRoom_User_UserIdAndSenderTypeAndIsRead(Long chatRoomUserUserId, SenderType senderType, boolean read);

    Integer countByChatRoom_Academy_User_UserIdAndSenderTypeAndIsRead(Long chatRoomAcademyUserUserId, SenderType senderType, boolean read);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM chat WHERE created_at < :sixMonthsAgo LIMIT :batchSize", nativeQuery = true)
    int bulkDeleteOldChatsWithLimit(@Param("sixMonthsAgo") LocalDateTime sixMonthsAgo, @Param("batchSize") int batchSize);


    List<Chat> findByChatRoom_ChatRoomIdAndSenderTypeAndIsRead(Long chatRoomChatRoomId, SenderType senderType, boolean read);
}

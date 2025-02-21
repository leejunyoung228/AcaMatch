package com.green.acamatch.chat;

import com.green.acamatch.chat.model.ChatRoomDto;
import com.green.acamatch.chat.model.ChatRoomGetReq;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.academy.ChatRoom;
import com.green.acamatch.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByUserAndAcademy(User user, Academy academy);

    @Query("""
    SELECT new com.green.acamatch.chat.model.ChatRoomDto(
        cr, ap, MAX(c1.createdAt), COALESCE(COUNT(c2.chatId), 0)
    )
    FROM ChatRoom cr
    LEFT JOIN Chat c1 ON cr.chatRoomId = c1.chatRoom.chatRoomId
    LEFT JOIN Chat c2 ON cr.chatRoomId = c2.chatRoom.chatRoomId AND c2.isRead = false AND c2.senderType = :#{#req.senderType}
    LEFT JOIN AcademyPic ap ON cr.academy = ap.academy
    WHERE (:#{#req.userId} IS NULL OR cr.user.userId = :#{#req.userId})
    AND (:#{#req.acaId} IS NULL OR cr.academy.acaId = :#{#req.acaId})
    GROUP BY cr.chatRoomId
    ORDER BY MAX(c1.createdAt) DESC
    """)
    Page<ChatRoomDto> getChatRooms(ChatRoomGetReq req, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM ChatRoom cr WHERE SIZE(cr.chats) = 0")
    void deleteAllByChatsIsEmpty();
}

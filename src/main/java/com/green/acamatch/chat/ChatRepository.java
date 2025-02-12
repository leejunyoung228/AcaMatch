package com.green.acamatch.chat;

import com.green.acamatch.chat.model.ChatUserList;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.academy.Chat;
import com.green.acamatch.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("""
        SELECT new com.green.acamatch.chat.model.ChatUserList(
                c.user.userId, c.user.name, c.user.userPic,
                c.academy.acaId, c.academy.acaName, c.academy.acaPic,
                c.createdAt, cnt.count
            ) FROM Chat AS c
            JOIN (
                SELECT COUNT(*) AS count, MAX(c.chatId) AS chatId FROM Chat c
                WHERE c.user.userId = :userId AND c.isRead = false
                GROUP BY c.academy, c.user
            ) AS cnt ON cnt.chatId = c.chatId
            WHERE c.user.userId = :userId
            GROUP BY c.academy
            ORDER BY c.chatId DESC
    """)
    Page<ChatUserList> findChatRoomByUserId(long userId, Pageable pageable);

    @Query("""
        SELECT new com.green.acamatch.chat.model.ChatUserList(
                c.user.userId, c.user.name, c.user.userPic,
                c.academy.acaId, c.academy.acaName, c.academy.acaPic,
                c.createdAt, cnt.count
            ) FROM Chat AS c
            JOIN (
                SELECT COUNT(*) AS count, MAX(c.chatId) AS chatId FROM Chat c
                WHERE c.academy.acaId = :acaId AND c.isRead = false
                GROUP BY c.academy, c.user
            ) AS cnt ON cnt.chatId = c.chatId
            WHERE c.academy.acaId = :acaId
            GROUP BY c.user
            ORDER BY c.chatId DESC
    """)
    Page<ChatUserList> findChatRoomByAcaId(long acaId, Pageable pageable);

    List<Chat> findAllByUserAndAcademyOrderByCreatedAtDesc(User user, Academy academy, Pageable pageable);

    Integer countChatByAcademyInAndSenderTypeAndIsRead(Collection<Academy> academies, Integer senderType, boolean read);

    Integer countChatByUserAndSenderTypeAndIsRead(User user, Integer senderType, boolean read);
}

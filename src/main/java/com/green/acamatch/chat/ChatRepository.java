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

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findAllByUserAndAcademyOrderByCreatedAtDesc(User user, Academy academy, Pageable pageable);

    @Query("""
                SELECT new com.green.acamatch.chat.model.ChatUserList(
                            c.user.userId, c.user.name,c.user.userPic,
                            c.academy.acaId, c.academy.acaName, c.academy.acaPic,
                            c.createdAt,
                            CASE WHEN c.senderType != 0 THEN 1 ELSE c.isRead END
                            ) FROM Chat c
                WHERE c.chatId IN (
                    SELECT MAX(c2.chatId) FROM Chat c2
                    WHERE c2.user = :user
                    GROUP BY c2.academy
                )
                ORDER BY c.createdAt DESC
           """)
    Page<ChatUserList> findByUser(User user, Pageable pageable);

    @Query("""
                SELECT new com.green.acamatch.chat.model.ChatUserList(
                            c.user.userId, c.user.name,c.user.userPic,
                            c.academy.acaId, c.academy.acaName, c.academy.acaPic,
                            c.createdAt,
                            CASE WHEN c.senderType != 1 THEN 1 ELSE c.isRead END
                            ) FROM Chat c
                WHERE c.chatId IN (
                    SELECT MAX(c2.chatId) FROM Chat c2
                    WHERE c2.academy = :academy
                    GROUP BY c2.user
                )
                ORDER BY c.createdAt DESC
            """)
    Page<ChatUserList> findByAcademy(Academy academy, Pageable pageable);
}

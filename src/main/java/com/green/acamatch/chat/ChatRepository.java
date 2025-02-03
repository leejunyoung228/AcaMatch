package com.green.acamatch.chat;

import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.academy.Chat;
import com.green.acamatch.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Page<Chat> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    Page<Chat> findAllByAcademyOrderByCreatedAtDesc(Academy academy, Pageable pageable);

    List<Chat> findAllByUserAndAcademyOrderByCreatedAtDesc(User user, Academy academy, Pageable pageable);
}

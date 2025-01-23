package com.green.acamatch.user.repository;

import com.green.acamatch.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickName(String nickName);

    User findUserByUserId(Long userId);
}

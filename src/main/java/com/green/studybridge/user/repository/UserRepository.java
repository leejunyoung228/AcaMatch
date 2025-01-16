package com.green.studybridge.user.repository;

import com.green.studybridge.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickName(String nickName);
}

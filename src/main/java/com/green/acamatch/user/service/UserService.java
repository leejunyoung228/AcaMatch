package com.green.acamatch.user.service;

import com.green.acamatch.config.security.AuthenticationFacade;
import com.green.acamatch.joinClass.JoinClassMapper;
import com.green.acamatch.joinClass.model.JoinClassRepository;
import com.green.acamatch.user.UserUtils;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.user.model.UserInfo;
import com.green.acamatch.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserUtils userUtils;
    private final UserRepository userRepository;
    private final JoinClassMapper joinClassMapper;
    private final JoinClassRepository joinClassRepository;

    // 특정 학원의 특정 수업을 듣는 학생(또는 학부모) 목록 조회
    public List<User> findStudentsByClassId(Long classId) {
        return joinClassRepository.findStudentsByClassId(classId);
    }

    public UserInfo getUserInfo() {
        User user = userUtils.findUserById(AuthenticationFacade.getSignedUserId());
        return UserInfo.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .userRole(user.getUserRole())
                .userPic(user.getUserPic())
                .email(user.getEmail())
                .birth(user.getBirth())
                .phone(user.getPhone())
                .nickName(user.getNickName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    public Long getTotalUserCount() {
        return userRepository.count();
    }
}

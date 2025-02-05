package com.green.acamatch.user.service;

import com.green.acamatch.config.security.AuthenticationFacade;
import com.green.acamatch.user.UserUtils;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.user.model.UserInfo;
import com.green.acamatch.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserUtils userUtils;
    private final UserRepository userRepository;

    public UserInfo getUserInfo() {
        User user = userUtils.findUserById(AuthenticationFacade.getSignedUserId());
        return UserInfo.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .roleId(user.getRole().getRoleId())
                .userPic(user.getUserPic())
                .email(user.getEmail())
                .birth(user.getBirth())
                .phone(user.getPhone())
                .nickName(user.getNickName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public Long getTotalUserCount() {
        return userRepository.count();
    }
}

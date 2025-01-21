package com.green.studybridge.user.service;

import com.green.studybridge.config.security.AuthenticationFacade;
import com.green.studybridge.user.UserUtils;
import com.green.studybridge.user.entity.User;
import com.green.studybridge.user.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserUtils userUtils;
    public UserInfo getUserInfo() {
        User user = userUtils.getUserById(AuthenticationFacade.getSignedUserId());
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
}

package com.green.acamatch.user.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class UserInfo {
    private long userId;
    private long roleId;
    private String name;
    private String email;
    private String phone;
    private LocalDate birth;
    private String nickName;
    private String userPic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

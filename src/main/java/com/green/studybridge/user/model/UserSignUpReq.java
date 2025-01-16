package com.green.studybridge.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserSignUpReq {
    private long roleId;
    private int signUpType;
    private String name;
    private String phone;
    private LocalDate birth;
    private String nickName;
    private String email;
    private String upw;
}

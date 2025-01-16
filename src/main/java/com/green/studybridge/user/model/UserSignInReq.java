package com.green.studybridge.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSignInReq {
    private String email;
    private String upw;
}

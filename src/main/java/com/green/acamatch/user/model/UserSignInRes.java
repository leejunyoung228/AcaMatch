package com.green.acamatch.user.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSignInRes {
    private long userId;
    private long roleId;
    private String name;
    private String accessToken;
}

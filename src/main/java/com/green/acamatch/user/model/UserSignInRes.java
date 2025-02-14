package com.green.acamatch.user.model;

import com.green.acamatch.entity.myenum.UserRole;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class UserSignInRes {
    private long userId;
    private UserRole userRole;
    private String name;
    private String accessToken;
}

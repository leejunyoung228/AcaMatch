package com.green.acamatch.config.security.oauth;

import com.green.acamatch.config.jwt.JwtUser;
import lombok.Getter;

import java.util.List;

@Getter
public class OAuth2JwtUser extends JwtUser {
    private final String email;
    private final String nickName;
    private final String pic;
    private final boolean needMoreData;

    public OAuth2JwtUser(String email, String nickName, String pic, long signedUserId, List<String> roles, boolean needMoreData) {
        super(signedUserId, roles);
        this.email = email;
        this.nickName = nickName;
        this.pic = pic;
        this.needMoreData = needMoreData;
    }
}

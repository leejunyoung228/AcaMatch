package com.green.acamatch.config.security.oauth;

import com.green.acamatch.config.jwt.JwtUser;
import com.green.acamatch.entity.myenum.SignInProviderType;
import lombok.Getter;

import java.util.List;

@Getter
public class OAuth2JwtUser extends JwtUser {
    private final String email;
    private final String nickName;
    private final String pic;
    private final SignInProviderType signInProviderType; // ✅ SNS 제공자 추가
    private boolean needMoreData;

    public OAuth2JwtUser(String email, String nickName, String pic, long signedUserId,
                         List<String> roles, boolean needMoreData, SignInProviderType signInProviderType) {
        super(signedUserId, roles);
        this.email = email;
        this.nickName = nickName;
        this.pic = pic;
        this.needMoreData = needMoreData;
        this.signInProviderType = signInProviderType; // ✅ 추가
    }

    // 회원가입이 완료된 경우 needMoreData 값을 false로 변경하는 메서드
    public void completeRegistration() {
        this.needMoreData = false;
    }

    // SNS 제공자 반환 메서드 추가
    public SignInProviderType getSignInProviderType() {
        return this.signInProviderType;
    }
}

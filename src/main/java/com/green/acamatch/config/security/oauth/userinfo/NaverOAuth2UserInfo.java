package com.green.acamatch.config.security.oauth.userinfo;

import java.util.Map;
import java.util.Optional;

/*
    naver user-info response JSON
{
  "resultcode": "00",
  "message": "success",
  "response": {
    "id": "1234567890",
    "email": "user@example.com",
    "nickname": "사용자닉네임",
    "profile_image": "https://profile.url"
  }
}
*/
public class NaverOAuth2UserInfo extends Oauth2UserInfo {

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }
    private Optional<Map<String, Object>> getResponse() {
        return Optional.ofNullable((Map<String, Object>) attributes.get("response"));
    }

    @Override
    public String getId() {
        return getResponse().get().get("id").toString();
    }

    @Override
    public String getName() {
        return getResponse().get().get("name").toString();
    }

    @Override
    public String getEmail() {
        return getResponse().get().get("email").toString();
    }

    @Override
    public String getProfileImageUrl() {
        return getResponse().get().get("profile_image").toString();
    }
}

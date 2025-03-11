package com.green.acamatch.config.security.oauth;

import com.green.acamatch.config.exception.CommonErrorCode;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.security.oauth.userinfo.Oauth2UserInfo;
import com.green.acamatch.config.security.oauth.userinfo.Oauth2UserInfoFactory;
import com.green.acamatch.entity.myenum.SignInProviderType;
import com.green.acamatch.entity.myenum.UserRole;
import com.green.acamatch.entity.user.SnsUser;
import com.green.acamatch.entity.user.SnsUserId;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.user.repository.SnsUserIdRepository;
import com.green.acamatch.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyOauth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final SnsUserIdRepository snsUserIdRepository;
    private final Oauth2UserInfoFactory oauth2UserInfoFactory;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest req) {
        try {
            return process(req);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest req) {
        OAuth2User oAuth2User = super.loadUser(req);
        /*
        req.getClientRegistration().getRegistrationId(); 소셜로그인 신청한 플랫폼 문자열값이 넘어온다.
        플랫폼 문자열값은 spring.security.oauth2.client.registration 아래에 있는 속성값들이다. (google, kakao, naver)
         */
        SignInProviderType signInProviderType = SignInProviderType.valueOf(req.getClientRegistration()
                .getRegistrationId()
                .toUpperCase());
        //사용하기 편하도록 규격화된 객체로 변환
        Oauth2UserInfo oauth2UserInfo = oauth2UserInfoFactory.getOauth2UserInfo(signInProviderType, oAuth2User.getAttributes());


        SnsUser snsUser = snsUserIdRepository.findBySnsUserIdAndId_SignInProviderType(oauth2UserInfo.getId(), signInProviderType);
        //기존에 회원가입이 되어있는지 체크
        User user;
        boolean needMoreData = false;
        if (snsUser == null) { // 최초 로그인 상황 > 회원가입 처리
            user = userRepository.findByEmail(oauth2UserInfo.getEmail()).orElse(null);
            if (user == null) {
                user = new User();
                user.setEmail(oauth2UserInfo.getEmail());
                user.setPhone("");
//                user.setUserPic(oauth2UserInfo.getProfileImageUrl());
                user.setUserRole(UserRole.NOT_SELECTED);
                user.setUpw("");
                user.setBirth(LocalDate.now());
                user.setName("");
                user.setNickName(UUID.randomUUID().toString());
                userRepository.save(user);
                needMoreData = true;
            }
            snsUser = new SnsUser();
            snsUser.setSnsUserId(oauth2UserInfo.getId());
            snsUser.setId(SnsUserId.builder()
                    .userId(user.getUserId())
                    .signInProviderType(signInProviderType)
                    .build());
            snsUser.setUser(user);
            snsUserIdRepository.save(snsUser);
        } else {
            user = userRepository.findById(snsUser.getUser().getUserId()).orElseThrow(() -> new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR));
        }
        if (user.getUserRole().equals(UserRole.NOT_SELECTED)) {
            needMoreData = true;
        }
        return new OAuth2JwtUser(user.getEmail(), user.getNickName()
                , user.getUserPic()
                , user.getUserId()
                , List.of(user.getUserRole().name())
                , needMoreData
                , signInProviderType);
    }
}







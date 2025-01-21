package com.green.studybridge.user.service;

import com.green.studybridge.config.CookieUtils;
import com.green.studybridge.config.constant.JwtConst;
import com.green.studybridge.config.exception.CustomException;
import com.green.studybridge.config.exception.UserErrorCode;
import com.green.studybridge.config.jwt.JwtTokenProvider;
import com.green.studybridge.config.jwt.JwtUser;
import com.green.studybridge.user.UserUtils;
import com.green.studybridge.user.entity.User;
import com.green.studybridge.user.model.UserSignInReq;
import com.green.studybridge.user.model.UserSignInRes;
import com.green.studybridge.user.model.UserSignUpReq;
import com.green.studybridge.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final EmailService emailService;
    private final SignUpUserCache signUpUserCache;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConst jwtConst;
    private final CookieUtils cookieUtils;
    private final UserUtils userUtils;

    public UserSignInRes signIn(UserSignInReq req, HttpServletResponse response) {
        User user = userRepository.getUserByEmail(req.getEmail());
        if (user == null || !passwordEncoder.matches(req.getUpw(), user.getUpw())) {
            throw new CustomException(UserErrorCode.INCORRECT_ID_PW);
        }

        return userUtils.generateUserSignInResByUser(user, response);
    }

    public void sendSignUpEmail(UserSignUpReq req) {
        userUtils.checkDuplicate(req.getEmail(), "email");
        userUtils.checkDuplicate(req.getNickName(), "nick-name");

        User user = userUtils.generateUserByUserSignUpReq(req);
        String token = UUID.randomUUID().toString();
        emailService.sendCodeToEmail(req.getEmail(), token);
        signUpUserCache.saveToken(token, user);
    }

    public String getAccessToken(HttpServletRequest request) {
        Cookie cookie = cookieUtils.getCookie(request, jwtConst.getRefreshTokenCookieName());
        String refreshToken = cookie.getValue();
        JwtUser jwtUser = jwtTokenProvider.getJwtUserFromToken(refreshToken);
        return jwtTokenProvider.generateAccessToken(jwtUser);
    }
}

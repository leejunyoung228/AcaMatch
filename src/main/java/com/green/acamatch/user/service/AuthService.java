package com.green.acamatch.user.service;

import com.green.acamatch.config.CookieUtils;
import com.green.acamatch.config.constant.JwtConst;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.UserErrorCode;
import com.green.acamatch.config.jwt.JwtTokenProvider;
import com.green.acamatch.config.jwt.JwtUser;
import com.green.acamatch.user.UserUtils;
import com.green.acamatch.user.entity.User;
import com.green.acamatch.user.model.UserSignInReq;
import com.green.acamatch.user.model.UserSignInRes;
import com.green.acamatch.user.model.UserSignUpReq;
import com.green.acamatch.user.repository.UserRepository;
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

    public int sendSignUpEmail(UserSignUpReq req) {
        userUtils.checkDuplicate(req.getEmail(), "email");
        userUtils.checkDuplicate(req.getNickName(), "nick-name");

        User user = userUtils.generateUserByUserSignUpReq(req);
        String token = UUID.randomUUID().toString();
        emailService.sendCodeToEmail(req.getEmail(), token);
        signUpUserCache.saveToken(token, user);
        return 1;
    }

    public String getAccessToken(HttpServletRequest request) {
        Cookie cookie = cookieUtils.getCookie(request, jwtConst.getRefreshTokenCookieName());
        String refreshToken = cookie.getValue();
        JwtUser jwtUser = jwtTokenProvider.getJwtUserFromToken(refreshToken);
        return jwtTokenProvider.generateAccessToken(jwtUser);
    }
}

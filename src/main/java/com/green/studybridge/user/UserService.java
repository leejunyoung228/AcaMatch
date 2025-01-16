package com.green.studybridge.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.studybridge.config.CookieUtils;
import com.green.studybridge.config.JwtConst;
import com.green.studybridge.config.MyFileUtils;
import com.green.studybridge.config.jwt.JwtTokenProvider;
import com.green.studybridge.config.jwt.JwtUser;
import com.green.studybridge.user.auth.AuthService;
import com.green.studybridge.user.entity.Role;
import com.green.studybridge.user.entity.User;
import com.green.studybridge.user.model.UserSignInRes;
import com.green.studybridge.user.model.UserSignUpReq;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MyFileUtils myFileUtils;
    private final SignUpUserCache signUpUserCache;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtils cookieUtils;
    private final JwtConst jwtConst;
    private final ObjectMapper objectMapper;

    @Transactional
    public void signUp(String token, HttpServletResponse response) {
        User user = signUpUserCache.verifyToken(token);
        userRepository.save(user);
        List<String> roles = new ArrayList<>();
        roles.add(user.getRole().getRoleName());
        JwtUser jwtUser = new JwtUser(user.getUserId(), roles);
        String accessToken = jwtTokenProvider.generateAccessToken(jwtUser);
        String refreshToken = jwtTokenProvider.generateRefreshToken(jwtUser);

        cookieUtils.setCookie(response, jwtConst.getRefreshTokenCookieName(), refreshToken, jwtConst.getRefreshTokenCookieExpiry());
        String userData = "";
        try {
            userData = objectMapper.writeValueAsString(UserSignInRes.builder()
                    .userId(user.getUserId())
                    .roleId(user.getRole().getRoleId())
                    .name(user.getName())
                    .accessToken(accessToken)
                    .build());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json 변환 에러");
        }
        String encodedData = Base64.getEncoder().encodeToString(userData.getBytes(StandardCharsets.UTF_8));
        Cookie signedUser = new Cookie("userData", encodedData);
        signedUser.setHttpOnly(true); // 클라이언트에서 접근 불가
        signedUser.setSecure(true);  // HTTPS에서만 전송
        signedUser.setPath("/");
        response.addCookie(signedUser);
    }

    public void sendEmail(UserSignUpReq req) {
        Role role = roleRepository.searchRoleByRoleId(req.getRoleId());
        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setUpw(passwordEncoder.encode(req.getUpw()));
        user.setBirth(req.getBirth());
        user.setPhone(req.getPhone());
        user.setNickName(req.getNickName());
        user.setSignUpType(req.getSignUpType());
        user.setRole(role);
        String token = UUID.randomUUID().toString();
        authService.sendCodeToEmail(req.getEmail(), "회원가입", token);
        signUpUserCache.saveToken(token, user);
    }
}

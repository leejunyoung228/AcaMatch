package com.green.studybridge.user.service;

import com.green.studybridge.config.CookieUtils;
import com.green.studybridge.config.JwtConst;
import com.green.studybridge.config.MyFileUtils;
import com.green.studybridge.config.jwt.JwtTokenProvider;
import com.green.studybridge.config.jwt.JwtUser;
import com.green.studybridge.user.entity.Role;
import com.green.studybridge.user.entity.User;
import com.green.studybridge.user.model.UserSignInReq;
import com.green.studybridge.user.model.UserSignInRes;
import com.green.studybridge.user.model.UserSignUpReq;
import com.green.studybridge.user.repository.RoleRepository;
import com.green.studybridge.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
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
    private final SignUpUserCache.AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtils cookieUtils;
    private final JwtConst jwtConst;

    @Transactional
    public void signUp(String token, HttpServletResponse response, HttpSession session) {
        User user = signUpUserCache.verifyToken(token);
        userRepository.save(user);
        JwtUser jwtUser = generateJwtUserByUser(user);
        String accessToken = jwtTokenProvider.generateAccessToken(jwtUser);
        String refreshToken = jwtTokenProvider.generateRefreshToken(jwtUser);

        cookieUtils.setCookie(response, jwtConst.getRefreshTokenCookieName(), refreshToken, jwtConst.getRefreshTokenCookieExpiry());
        session.setAttribute("signedUser", UserSignInRes.builder()
                .userId(user.getUserId())
                .roleId(user.getRole().getRoleId())
                .name(user.getName())
                .accessToken(accessToken)
                .build());
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public UserSignInRes signIn(UserSignInReq req, HttpServletResponse response) {
        User user = userRepository.getUserByEmail(req.getEmail());
        if (user == null || !passwordEncoder.matches(req.getUpw(), user.getUpw())) {
            throw new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다");
        }

        JwtUser jwtUser = generateJwtUserByUser(user);
        String accessToken = jwtTokenProvider.generateAccessToken(jwtUser);
        String refreshToken = jwtTokenProvider.generateRefreshToken(jwtUser);

        cookieUtils.setCookie(response, jwtConst.getRefreshTokenCookieName(), refreshToken, jwtConst.getRefreshTokenCookieExpiry());

        return UserSignInRes.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .roleId(user.getRole().getRoleId())
                .accessToken(accessToken)
                .build();
    }
    private JwtUser generateJwtUserByUser(User user) {
        List<String> roles = new ArrayList<>();
        roles.add(user.getRole().getRoleName());
        return new JwtUser(user.getUserId(), roles);
    }

    public int checkDuplicate(String text, String type) {
        if (type.equals("nick-name")) {
            if(userRepository.existsByNickName(text)) {
                throw new RuntimeException("닉네임이 중복되었습니다");
            }
            return 1;
        }
        if (type.equals("email")) {
            if (userRepository.existsByEmail(text)) {
                throw new RuntimeException("이메일이 중복되었습니다");
            }
            return 1;
        }
        throw new RuntimeException("지정된 타입이 아닙니다.");
    }
}

package com.green.acamatch.user;

import com.green.acamatch.config.CookieUtils;
import com.green.acamatch.config.constant.JwtConst;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.UserErrorCode;
import com.green.acamatch.config.jwt.JwtTokenProvider;
import com.green.acamatch.config.jwt.JwtUser;
import com.green.acamatch.entity.user.Role;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.user.model.UserSignInRes;
import com.green.acamatch.user.model.UserSignUpReq;
import com.green.acamatch.user.repository.RoleRepository;
import com.green.acamatch.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserUtils {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtils cookieUtils;
    private final JwtConst jwtConst;

    public int checkDuplicate(String text, String type) {
        switch (type) {
            case "nick-name":
                if (userRepository.existsByNickName(text)) {
                    throw new CustomException(UserErrorCode.DUPLICATE_USER_NICKNAME);
                }
                break;
            case "email":
                if (userRepository.existsByEmail(text)) {
                    throw new CustomException(UserErrorCode.DUPLICATE_USER_EMAIL);
                }
                break;
            default:
                throw new CustomException(UserErrorCode.INCORRECT_DUPLICATE_CHECK_TYPE);
        }
        return 1;
    }

    public User generateUserByUserSignUpReq(UserSignUpReq req) {
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
        return user;
    }

    public UserSignInRes generateUserSignInResByUser(User user, HttpServletResponse response) {
        List<String > roles = new ArrayList<>();
        roles.add(user.getRole().getRoleName());
        JwtUser jwtUser = new JwtUser(user.getUserId(), roles);

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

    public User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
    }
}

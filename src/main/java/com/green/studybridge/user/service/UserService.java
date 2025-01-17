package com.green.studybridge.user.service;

import com.green.studybridge.config.CookieUtils;
import com.green.studybridge.config.JwtConst;
import com.green.studybridge.config.MyFileUtils;
import com.green.studybridge.config.jwt.JwtTokenProvider;
import com.green.studybridge.config.jwt.JwtUser;
import com.green.studybridge.config.security.AuthenticationFacade;
import com.green.studybridge.user.entity.Role;
import com.green.studybridge.user.entity.User;
import com.green.studybridge.user.model.*;
import com.green.studybridge.user.repository.RoleRepository;
import com.green.studybridge.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationFacade authenticationFacade;
    private final CookieUtils cookieUtils;
    private final JwtConst jwtConst;

    @Transactional
    public UserSignInRes signUp(String token, HttpServletResponse response) {
        User user = signUpUserCache.verifyToken(token);
        userRepository.save(user);

        return generateUserSignInResByUser(user, response);
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

        return generateUserSignInResByUser(user, response);
    }

    private JwtUser generateJwtUserByUser(User user) {
        List<String> roles = new ArrayList<>();
        roles.add(user.getRole().getRoleName());
        return new JwtUser(user.getUserId(), roles);
    }

    private UserSignInRes generateUserSignInResByUser(User user, HttpServletResponse response) {
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

    public int checkDuplicate(String text, String type) {
        if (type.equals("nick-name")) {
            if (userRepository.existsByNickName(text)) {
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

    public void updateUserPic(MultipartFile pic) {
        long userId = authenticationFacade.getSignedUserId();
        User user = getUserById(userId);
        String prePic = user.getUserPic();
        user.setUserPic(myFileUtils.makeRandomFileName(pic));
        userRepository.save(user);
        String folderPath = String.format("/user/%d", userId);
        if (prePic != null) {
            myFileUtils.deleteFolder(folderPath, false);
        }
        myFileUtils.makeFolders(folderPath);
        String filePath = String.format("%s/%s", folderPath, user.getUserPic());
        try {
            myFileUtils.transferTo(pic, filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateUser(UserUpdateReq req) {
        User user = getUserById(authenticationFacade.getSignedUserId());
        user.setName(req.getName());
        user.setNickName(req.getNickName());
        user.setBirth(req.getBirth());
        user.setPhone(req.getPhone());
        userRepository.save(user);
    }

    public UserInfo getUserInfo() {
        User user = getUserById(authenticationFacade.getSignedUserId());
        return UserInfo.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .roleId(user.getRole().getRoleId())
                .userPic(user.getUserPic())
                .email(user.getEmail())
                .birth(user.getBirth())
                .phone(user.getPhone())
                .nickName(user.getNickName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    private User getUserById(long userId) {
        return userRepository.findById(authenticationFacade.getSignedUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
    }
}

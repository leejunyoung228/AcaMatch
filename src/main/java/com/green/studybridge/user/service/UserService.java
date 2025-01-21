package com.green.studybridge.user.service;

import com.green.studybridge.config.CookieUtils;
import com.green.studybridge.config.MyFileUtils;
import com.green.studybridge.config.constant.UserConst;
import com.green.studybridge.config.exception.CommonErrorCode;
import com.green.studybridge.config.exception.CustomException;
import com.green.studybridge.config.exception.UserErrorCode;
import com.green.studybridge.config.jwt.JwtTokenProvider;
import com.green.studybridge.config.jwt.JwtUser;
import com.green.studybridge.config.security.AuthenticationFacade;
import com.green.studybridge.config.constant.JwtConst;
import com.green.studybridge.user.entity.Role;
import com.green.studybridge.user.entity.User;
import com.green.studybridge.user.model.*;
import com.green.studybridge.user.repository.RoleRepository;
import com.green.studybridge.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final SignUpUserCache signUpUserCache;
    private final MyFileUtils myFileUtils;
    private final CookieUtils cookieUtils;
    private final JwtConst jwtConst;
    private final UserConst userConst;

    @Transactional
    public UserSignInRes signUp(String token, HttpServletResponse response) {
        User user = signUpUserCache.verifyToken(token);
        userRepository.save(user);

        return generateUserSignInResByUser(user, response);
    }

    public void sendEmail(UserSignUpReq req) {
        User user = generateUserByUserSignUpReq(req);
        String token = UUID.randomUUID().toString();
        authService.sendCodeToEmail(req.getEmail(), token);
        signUpUserCache.saveToken(token, user);
    }

    public UserSignInRes signIn(UserSignInReq req, HttpServletResponse response) {
        User user = userRepository.getUserByEmail(req.getEmail());
        if (user == null || !passwordEncoder.matches(req.getUpw(), user.getUpw())) {
            throw new CustomException(UserErrorCode.INCORRECT_ID_PW);
        }

        return generateUserSignInResByUser(user, response);
    }

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

    public void updateUserPic(MultipartFile pic) {
        long userId = AuthenticationFacade.getSignedUserId();
        User user = getUserById(userId);

        String prePic = user.getUserPic();
        user.setUserPic(myFileUtils.makeRandomFileName(pic));
        userRepository.save(user);

        String folderPath = String.format(userConst.getUserPicFilePath(), userId);
        if (prePic != null) {
            myFileUtils.deleteFolder(folderPath, false);
        }
        myFileUtils.makeFolders(folderPath);
        String filePath = String.format("%s/%s", folderPath, user.getUserPic());
        try {
            myFileUtils.transferTo(pic, filePath);
        } catch (IOException e) {
            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public void updateUser(UserUpdateReq req) {
        User user = getUserById(AuthenticationFacade.getSignedUserId());
        user.setName(req.getName());
        user.setNickName(req.getNickName());
        user.setBirth(req.getBirth());
        user.setPhone(req.getPhone());
        userRepository.save(user);
    }

    public UserInfo getUserInfo() {
        User user = getUserById(AuthenticationFacade.getSignedUserId());
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

    @Transactional
    public void deleteUser(@Valid UserDeleteReq req) {
        long userId = AuthenticationFacade.getSignedUserId();
        User user = getUserById(userId);
        if (!passwordEncoder.matches(req.getPw(), user.getUpw())) {
            throw new CustomException(UserErrorCode.INCORRECT_PW);
        }
        userRepository.deleteById(userId);
        String folderPath = String.format(userConst.getUserPicFilePath(), userId);
        myFileUtils.deleteFolder(folderPath, true);
    }

    public String getAccessToken(HttpServletRequest request) {
        Cookie cookie = cookieUtils.getCookie(request, jwtConst.getRefreshTokenCookieName());
        String refreshToken = cookie.getValue();
        JwtUser jwtUser = jwtTokenProvider.getJwtUserFromToken(refreshToken);
        return jwtTokenProvider.generateAccessToken(jwtUser);
    }


//--------------------------------------------------------------------------------------------------------------------------------------



    private User generateUserByUserSignUpReq(UserSignUpReq req) {
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

    private UserSignInRes generateUserSignInResByUser(User user, HttpServletResponse response) {
        JwtUser jwtUser = new JwtUser(user);

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

    private User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
    }
}

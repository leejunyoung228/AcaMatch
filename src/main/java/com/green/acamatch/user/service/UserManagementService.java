package com.green.acamatch.user.service;

import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.config.constant.UserConst;
import com.green.acamatch.config.exception.CommonErrorCode;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.UserErrorCode;
import com.green.acamatch.config.security.AuthenticationFacade;
import com.green.acamatch.user.UserUtils;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.user.model.SimpleUserDataUpdateReq;
import com.green.acamatch.user.model.UserDeleteReq;
import com.green.acamatch.user.model.UserUpdateReq;
import com.green.acamatch.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserManagementService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserCache userCache;
    private final UserUtils userUtils;
    private final MyFileUtils myFileUtils;
    private final UserConst userConst;

    @Transactional
    public void signUp(String token, HttpServletResponse response) {
        User user = userCache.verifyToken(token);
        userRepository.save(user);
        redirectTo(response, userConst.getRedirectUrl());
    }

    @Transactional
    public int updateUser(UserUpdateReq req, MultipartFile pic) {
        User user = userUtils.findUserById(AuthenticationFacade.getSignedUserId());
        if (req.getCurrentPw() == null || !passwordEncoder.matches(req.getCurrentPw(), user.getUpw())) {
            throw new CustomException(UserErrorCode.INCORRECT_PW);
        }
        if (req.getName() != null) user.setName(req.getName());
        if (req.getNickName() != null) user.setNickName(req.getNickName());
        if (req.getBirth() != null) user.setBirth(req.getBirth());
        if (req.getPhone() != null) user.setPhone(req.getPhone());
        if (req.getNewPw() != null) user.setUpw(passwordEncoder.encode(req.getNewPw()));
        if (pic != null && !pic.isEmpty()) updateUserProfile(user, pic);

        userRepository.save(user);

        return 1;
    }

    @Transactional
    public int deleteUser(UserDeleteReq req) {
        long userId = AuthenticationFacade.getSignedUserId();
        User user = userUtils.findUserById(userId);
        if (!passwordEncoder.matches(req.getPw(), user.getUpw())) {
            throw new CustomException(UserErrorCode.INCORRECT_PW);
        }
        userRepository.deleteById(userId);
        String folderPath = String.format(userConst.getUserPicFilePath(), userId);
        myFileUtils.deleteFolder(folderPath, true);
        return 1;
    }

    public void setTempPw(long id, HttpServletResponse response) {
        String pw = userCache.getTempPw(id);
        User user = userUtils.findUserById(id);
        user.setUpw(passwordEncoder.encode(pw));
        userRepository.save(user);
        redirectTo(response, userConst.getRedirectUrl());
    }

    private void updateUserProfile(User user, MultipartFile pic) {
        String prePic = user.getUserPic();
        String folderPath = String.format(userConst.getUserPicFilePath(), user.getUserId());
        user.setUserPic(myFileUtils.makeRandomFileName(pic));
        String filePath = String.format("%s/%s", folderPath, user.getUserPic());
        if (prePic != null) {
            myFileUtils.deleteFolder(folderPath, false);
        }
        myFileUtils.makeFolders(folderPath);
        try {
            myFileUtils.transferTo(pic, filePath);
        } catch (IOException e) {
            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private void redirectTo(HttpServletResponse response, String url) {
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateSimpleUser(SimpleUserDataUpdateReq req) {
        User user = userUtils.findUserById(req.getUserId());
        user.setUserRole(req.getUserRole());
        user.setName(req.getName());
        user.setUpw(passwordEncoder.encode(req.getUpw()));
        user.setBirth(req.getBirth());
        user.setPhone(req.getPhone());
        user.setNickName(req.getNickName());
        userRepository.save(user);
    }
}

package com.green.acamatch.user.service;

import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.config.constant.UserConst;
import com.green.acamatch.config.exception.CommonErrorCode;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.UserErrorCode;
import com.green.acamatch.config.security.AuthenticationFacade;
import com.green.acamatch.user.UserUtils;
import com.green.acamatch.user.entity.User;
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
        try {
            response.sendRedirect(userConst.getRedirectUrl());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public int updateUser(UserUpdateReq req, MultipartFile pic) {
        User user = userUtils.getUserById(AuthenticationFacade.getSignedUserId());
        if (!passwordEncoder.matches(req.getCurrentPw(), user.getUpw())) {
            throw new CustomException(UserErrorCode.INCORRECT_PW);
        }
        if (req.getName() != null) user.setName(req.getName());
        if (req.getNickName() != null) user.setNickName(req.getNickName());
        if (req.getBirth() != null) user.setBirth(req.getBirth());
        if (req.getPhone() != null) user.setPhone(req.getPhone());
        if (req.getNewPw() != null) user.setUpw(passwordEncoder.encode(req.getNewPw()));
        if (pic != null) {
            String prePic = user.getUserPic();
            user.setUserPic(myFileUtils.makeRandomFileName(pic));
            String folderPath = String.format(userConst.getUserPicFilePath(), user.getUserId());
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
        userRepository.save(user);

        return 1;
    }

    @Transactional
    public int deleteUser(UserDeleteReq req) {
        long userId = AuthenticationFacade.getSignedUserId();
        User user = userUtils.getUserById(userId);
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
        User user = userUtils.getUserById(id);
        user.setUpw(passwordEncoder.encode(pw));
        userRepository.save(user);
        try {
            response.sendRedirect(userConst.getRedirectUrl());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

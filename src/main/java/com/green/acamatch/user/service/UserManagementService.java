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
import com.green.acamatch.user.model.UserSignInRes;
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
    private final SignUpUserCache signUpUserCache;
    private final UserUtils userUtils;
    private final MyFileUtils myFileUtils;
    private final UserConst userConst;

    @Transactional
    public UserSignInRes signUp(String token, HttpServletResponse response) {
        User user = signUpUserCache.verifyToken(token);
        userRepository.save(user);

        return userUtils.generateUserSignInResByUser(user, response);
    }

    public int updateUserPic(MultipartFile pic) {
        long userId = AuthenticationFacade.getSignedUserId();
        User user = userUtils.getUserById(userId);

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
        return 1;
    }

    public int updateUser(UserUpdateReq req) {
        User user = userUtils.getUserById(AuthenticationFacade.getSignedUserId());
        if(req.getName() != null) user.setName(req.getName());
        if(req.getNickName() != null) user.setNickName(req.getNickName());
        if(req.getBirth() != null) user.setBirth(req.getBirth());
        if(req.getPhone() != null) user.setPhone(req.getPhone());
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
}

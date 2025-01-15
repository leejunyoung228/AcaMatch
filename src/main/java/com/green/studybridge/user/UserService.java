package com.green.studybridge.user;

import com.green.studybridge.config.MyFileUtils;
import com.green.studybridge.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final MyFileUtils myFileUtils;

    @Transactional
    public int signUp(User req, MultipartFile mf) {
        req.setUserPic(myFileUtils.makeRandomFileName(mf));
        userRepository.save(req);
        String folderPath = String.format("/user/%d", req.getUserId());
        myFileUtils.makeFolders(folderPath);
        String filePath = String.format("%s/%s", folderPath, req.getUserPic());
        try {
            myFileUtils.transferTo(mf, filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }

    public void sendEmail(User req, MultipartFile mf) {
        UserInfo userInfo = new UserInfo(req, mf);
    }
}

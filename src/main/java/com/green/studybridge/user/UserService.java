package com.green.studybridge.user;

import com.green.studybridge.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void signUp(User req) {
        userRepository.save(req);
        log.info(String.valueOf(req.getUserId()));
    }
}

package com.green.studybridge.user;

import com.green.studybridge.config.model.ResultResponse;
import com.green.studybridge.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("sign-up")
    public ResultResponse<Integer> signUp(@RequestPart User req, @RequestPart("pic") MultipartFile mf) {
//        int result = userService.signUp(req, mf);
//        return ResultResponse.<Integer> builder().resultMessage("회원 가입 성공").resultData(result).build();
        userService.sendEmail(req, mf);
        return null;
    }
}

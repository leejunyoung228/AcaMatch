package com.green.studybridge.user;

import com.green.studybridge.config.model.ResultResponse;
import com.green.studybridge.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("sign-up")
    public ResultResponse<Integer> signUp(@RequestBody User req) {
        userService.signUp(req);
        return null;
    }
}

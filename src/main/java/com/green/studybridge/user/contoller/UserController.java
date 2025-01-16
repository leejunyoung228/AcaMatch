package com.green.studybridge.user.contoller;

import com.green.studybridge.config.model.ResultResponse;
import com.green.studybridge.user.service.UserService;
import com.green.studybridge.user.model.UserSignInReq;
import com.green.studybridge.user.model.UserSignInRes;
import com.green.studybridge.user.model.UserSignUpReq;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("sign-up")
    public ResultResponse<Integer> signUp(@RequestBody UserSignUpReq req) {
        userService.sendEmail(req);
        return null;
    }

    @PostMapping("sign-in")
    public ResultResponse<UserSignInRes> signIn(@RequestBody UserSignInReq req, HttpServletResponse response) {
        UserSignInRes res = userService.signIn(req, response);
        return ResultResponse.<UserSignInRes>builder()
                .resultData(res)
                .build();
    }

    @Operation(description = "<strong>type</strong> : email 또는 nick-name 중 택 일</br><strong>text</strong> : 검사할 문자")
    @GetMapping("check-duplicate/{type}")
    public ResultResponse<Integer> checkDuplicate(@RequestParam String text, @PathVariable String type) {
        int res = userService.checkDuplicate(text, type);
        return ResultResponse.<Integer>builder().resultData(res).build();
    }
}

package com.green.studybridge.user.contoller;

import com.green.studybridge.config.model.ResultResponse;
import com.green.studybridge.user.UserUtils;
import com.green.studybridge.user.model.*;
import com.green.studybridge.user.service.AuthService;
import com.green.studybridge.user.service.UserManagementService;
import com.green.studybridge.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final UserManagementService userManagementService;
    private final UserUtils userUtils;

    @PostMapping("sign-up")
    @Operation(description = "이메일 인증 링크를 누르게 되면 /user/complete-sign-up?token=토큰 값 으로 이동</br>" +
            "token 값을 /user/sign-up?token=토큰값 으로 보내주세요</br>" +
            "<table border=\"1\"><thead><tr><th>ROLE_ID</th><th>Title</th></tr></thead><tbody><tr><td>1</td><td>ROLE_STUDENT</td></tr><tr><td>2</td><td>ROLE_PARENT</td></tr><tr><td>3</td><td>ROLE_ACADEMY</td></tr><tr><td>4</td><td>ROLE_TEACHER</td></tr></tbody></table>"
    )
    public ResultResponse<Integer> signUp(@Valid @RequestBody UserSignUpReq req) {
        authService.sendSignUpEmail(req);
        return null;
    }

    @GetMapping("sign-up")
    public ResultResponse<UserSignInRes> finishSignUp(String token, HttpServletResponse response) {
        return ResultResponse.<UserSignInRes>builder().resultData(userManagementService.signUp(token, response)).build();
    }

    @PostMapping("sign-in")
    public ResultResponse<UserSignInRes> signIn(@Valid @RequestBody UserSignInReq req, HttpServletResponse response) {
        UserSignInRes res = authService.signIn(req, response);
        return ResultResponse.<UserSignInRes>builder()
                .resultData(res)
                .build();
    }

    @GetMapping
    public ResultResponse<UserInfo> getUserInfo() {
        return ResultResponse.<UserInfo>builder()
                .resultData(userService.getUserInfo())
                .build();
    }

    @Operation(description = "<strong>type</strong> : email 또는 nick-name 중 택 일</br><strong>text</strong> : 검사할 문자")
    @GetMapping("check-duplicate/{type}")
    public ResultResponse<Integer> checkDuplicate(@RequestParam String text, @PathVariable String type) {
        int res = userUtils.checkDuplicate(text, type);
        return ResultResponse.<Integer>builder().resultData(res).build();
    }

    @PatchMapping
    public ResultResponse<Integer> updateUserPic(@RequestPart MultipartFile pic) {
        userManagementService.updateUserPic(pic);
        return ResultResponse.<Integer>builder()
                .resultData(1)
                .build();
    }

    @PutMapping
    public ResultResponse<Integer> updateUser(@Valid @RequestBody UserUpdateReq req) {
        userManagementService.updateUser(req);
        return ResultResponse.<Integer>builder()
                .resultData(1)
                .build();
    }

    @DeleteMapping
    public ResultResponse<Integer> deleteUser(@RequestBody UserDeleteReq req) {
        userManagementService.deleteUser(req);
        return ResultResponse.<Integer>builder().resultData(1).build();
    }

    @GetMapping("access-token")
    public ResultResponse<String> getAccessToken(HttpServletRequest request) {
        return ResultResponse.<String>builder()
                .resultData(authService.getAccessToken(request))
                .build();
    }
}

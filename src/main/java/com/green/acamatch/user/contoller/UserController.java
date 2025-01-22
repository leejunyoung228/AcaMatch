package com.green.acamatch.user.contoller;

import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.user.UserUtils;
import com.green.acamatch.user.model.*;
import com.green.acamatch.user.service.AuthService;
import com.green.acamatch.user.service.UserManagementService;
import com.green.acamatch.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
@Tag(name = "유저 관리 API")
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final UserManagementService userManagementService;
    private final UserUtils userUtils;

    @PostMapping("sign-up")
    @Operation(summary = "회원가입 이메일 전송",
            description = "이메일 인증 링크를 누르게 되면 /user/complete-sign-up?token=토큰 값 으로 이동</br>" +
                    "token 값을 /user/sign-up?token=토큰값 으로 보내주세요</br>" +
                    "<table border=\"1\"><thead><tr><th>ROLE_ID</th><th>Title</th></tr></thead><tbody><tr><td>1</td><td>ROLE_STUDENT</td></tr><tr><td>2</td><td>ROLE_PARENT</td></tr><tr><td>3</td><td>ROLE_ACADEMY</td></tr><tr><td>4</td><td>ROLE_TEACHER</td></tr></tbody></table>" +
                    "비밀번호는 대문자, 소문자, 특수문자, 숫자 포함 8자 이상 20자 미만 "
    )
    public ResultResponse<Integer> signUp(@Valid @RequestBody UserSignUpReq req) {
        authService.sendSignUpEmail(req);
        return ResultResponse.<Integer>builder()
                .resultMessage("이메일 발송 성공")
                .resultData(1)
                .build();
    }

    @GetMapping("sign-up")
    @Operation(summary = "회원가입 완료")
    public ResultResponse<UserSignInRes> finishSignUp(String token, HttpServletResponse response) {
        return ResultResponse.<UserSignInRes>builder()
                .resultMessage("회원 가입 성공")
                .resultData(userManagementService.signUp(token, response))
                .build();
    }

    @PostMapping("sign-in")
    @Operation(summary = "로그인")
    public ResultResponse<UserSignInRes> signIn(@Valid @RequestBody UserSignInReq req, HttpServletResponse response) {
        UserSignInRes res = authService.signIn(req, response);
        return ResultResponse.<UserSignInRes>builder()
                .resultMessage("로그인 성공")
                .resultData(res)
                .build();
    }

    @GetMapping
    @Operation(summary = "회원 정보 조회")
    public ResultResponse<UserInfo> getUserInfo() {
        return ResultResponse.<UserInfo>builder()
                .resultMessage("회원 정보 조회 성공")
                .resultData(userService.getUserInfo())
                .build();
    }

    @GetMapping("check-duplicate/{type}")
    @Operation(
            summary = "중복 체크",
            description = "<strong>type</strong> : email 또는 nick-name 중 택 일</br><strong>text</strong> : 검사할 문자"
    )
    public ResultResponse<Integer> checkDuplicate(@RequestParam String text, @PathVariable String type) {
        return ResultResponse.<Integer>builder()
                .resultMessage("중복 되지 않습니다")
                .resultData(userUtils.checkDuplicate(text, type))
                .build();
    }

    @PatchMapping
    @Operation(summary = "회원 프로필 사진 업로드")
    public ResultResponse<Integer> updateUserPic(@RequestPart MultipartFile pic) {
        userManagementService.updateUserPic(pic);
        return ResultResponse.<Integer>builder()
                .resultMessage("프로필 사진 업로드 성공")
                .resultData(1)
                .build();
    }

    @PutMapping
    @Operation(summary = "회원 정보 수정")
    public ResultResponse<Integer> updateUser(@Valid @RequestBody UserUpdateReq req) {
        userManagementService.updateUser(req);
        return ResultResponse.<Integer>builder()
                .resultMessage("회원 정보 수정 성공")
                .resultData(1)
                .build();
    }

    @DeleteMapping
    @Operation(summary = "회원 탈퇴")
    public ResultResponse<Integer> deleteUser(@RequestBody UserDeleteReq req) {
        userManagementService.deleteUser(req);
        return ResultResponse.<Integer>builder()
                .resultMessage("회원 탈퇴 성공")
                .resultData(1)
                .build();
    }

    @GetMapping("access-token")
    @Operation(summary = "액세스 토큰 재발행")
    public ResultResponse<String> getAccessToken(HttpServletRequest request) {
        return ResultResponse.<String>builder()
                .resultMessage("액세스 토큰 재발행 성공")
                .resultData(authService.getAccessToken(request))
                .build();
    }
}

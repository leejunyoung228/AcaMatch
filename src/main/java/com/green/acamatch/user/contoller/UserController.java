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
            description = "이메일 인증 링크를 누르게 되면 회원가입후 로그인 페이지로 이동</br>" +
                    "userRole : [ADMIN, STUDENT, PARENT, ACADEMY, TEACHER] 택 1</br>" +
                    "비밀번호는 대문자, 소문자, 특수문자, 숫자 포함 8자 이상 20자 미만 "
    )
    public ResultResponse<Integer> signUp(@Valid @RequestBody UserSignUpReq req) {
        int res = authService.sendSignUpEmail(req);
        return ResultResponse.<Integer>builder()
                .resultMessage("이메일 발송 성공")
                .resultData(res)
                .build();
    }

    @GetMapping("sign-up")
    @Operation(summary = "회원가입 완료(프론트에서 사용 안함)")
    public void finishSignUp(String token, HttpServletResponse response) {
        userManagementService.signUp(token, response);
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
    @Operation(summary = "회원 정보 조회(로그인 필수)")
    public ResultResponse<UserInfo> getUserInfo() {
        UserInfo res = userService.getUserInfo();
        return ResultResponse.<UserInfo>builder()
                .resultMessage("회원 정보 조회 성공")
                .resultData(res)
                .build();
    }

    @GetMapping("check-duplicate/{type}")
    @Operation(
            summary = "중복 체크",
            description = "<strong>type</strong> : email 또는 nick-name 중 택 일</br><strong>text</strong> : 검사할 문자"
    )
    public ResultResponse<Integer> checkDuplicate(@RequestParam String text, @PathVariable String type) {
        int res = userUtils.checkDuplicate(text, type);
        return ResultResponse.<Integer>builder()
                .resultMessage("중복 되지 않습니다")
                .resultData(res)
                .build();
    }

    @PutMapping
    @Operation(summary = "회원 정보 수정(로그인 필수)", description = "현재 비밀번호(currentPw)는 필수 나머지는 선택")
    public ResultResponse<Integer> updateUser(@Valid @RequestPart UserUpdateReq req, @RequestPart(required = false) MultipartFile pic) {
        int res = userManagementService.updateUser(req, pic);
        return ResultResponse.<Integer>builder()
                .resultMessage("회원 정보 수정 성공")
                .resultData(res)
                .build();
    }

    @PostMapping("temp-pw")
    @Operation(summary = "임시 비밀번호 요청")
    public ResultResponse<Integer> tempPwRequest(@Valid @RequestBody FindPwReq req) {
        int res = authService.sendTempPwEmail(req);
        return ResultResponse.<Integer>builder()
                .resultMessage("임시 비밀번호 전송 성공")
                .resultData(res)
                .build();
    }

    @GetMapping("temp-pw/{id}")
    @Operation(summary = "임시 비밀번호 저장(프론트에서 사용 안함)")
    public void getTempPw(HttpServletResponse response, @PathVariable long id) {
        userManagementService.setTempPw(id, response);
    }

    @DeleteMapping
    @Operation(summary = "회원 탈퇴(로그인 필수)")
    public ResultResponse<Integer> deleteUser(@Valid @RequestBody UserDeleteReq req) {
        int res = userManagementService.deleteUser(req);
        return ResultResponse.<Integer>builder()
                .resultMessage("회원 탈퇴 성공")
                .resultData(res)
                .build();
    }


    @PostMapping("log-out")
    @Operation(summary = "로그아웃(로그인 필수)", description = "쿠키 삭제")
    public ResultResponse<Integer> logout(HttpServletResponse response) {
        int res = authService.logOutUser(response);
        return ResultResponse.<Integer>builder()
                .resultMessage("로그아웃 성공")
                .resultData(res)
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

    @GetMapping("total_user")
    @Operation(summary = "유저수 조회")
    public ResultResponse<Long> getTotalUser() {
        return ResultResponse.<Long>builder()
                .resultMessage("유저수 조회 성공")
                .resultData(userService.getTotalUserCount())
                .build();
    }

    @PostMapping("simple-login-user-data")
    @Operation(summary = "간편 로그인으로 회원가입하는 유저 정보 업데이트",
            description = "userRole : [ADMIN, STUDENT, PARENT, ACADEMY, TEACHER] 택 1</br>" +
                    "비밀번호는 대문자, 소문자, 특수문자, 숫자 포함 8자 이상 20자 미만 "
    )
    public ResultResponse<Boolean> updateSimpleUserData(@Valid @RequestBody SimpleUserDataUpdateReq req) {
        userManagementService.updateSimpleUser(req);
        return ResultResponse.<Boolean>builder()
                .resultMessage("간편 로그인 유저 정보 업데이트 성공")
                .resultData(true)
                .build();
    }
}

package com.green.acamatch.manager;

import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.manager.model.TeacherRegisterReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Teacher Management", description = "선생님 관리 API")
@RestController
@RequestMapping("teacher")
@RequiredArgsConstructor
public class SmsTeacherController {

    private final SmsTeacherService smsTeacherService;

    @PostMapping("/register")
    @Operation(summary = "선생님 등록", description = "학원에 선생님을 등록합니다.")
    public ResultResponse<String> registerTeacher(@RequestBody TeacherRegisterReq req) {
        smsTeacherService.registerTeacher(req);
        return ResultResponse.<String>builder()
                .resultMessage("선생님 등록이 완료되었습니다.")
                .resultData("등록 성공")
                .build();
    }
}

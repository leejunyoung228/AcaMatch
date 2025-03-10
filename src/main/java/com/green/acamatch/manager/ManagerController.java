package com.green.acamatch.manager;

import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.manager.model.GetAcademyCostCountRes;
import com.green.acamatch.manager.model.GetAcademyCountRes;
import com.green.acamatch.manager.model.GetUserCountRes;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/academy-manager")
@RequiredArgsConstructor
public class ManagerController {
    private final ManagerService managerService;

    // 특정 수업 학생들에게 등하원 문자 발송 API
    @PostMapping("/send-attendance/class/{classId}")
    public ResultResponse<String> sendAttendanceToClass(@RequestParam Long senderId,
                                                        @PathVariable Long classId,
                                                        @RequestParam String message) {
        managerService.sendAttendanceNotificationByClass(senderId, classId, message);
        return ResultResponse.<String>builder()
                .resultMessage("수업 학생들에게 등하원 알림 문자 전송 완료.")
                .resultData("수업 ID: " + classId)
                .build();
    }

    @GetMapping("GetAcademyCount/{month}")
    @Operation(summary = "월별 등록한 학원 수", description = "이번달, 지난달 넣어서 테스트 해보시면 됩니다.")
    public ResultResponse<List<GetAcademyCountRes>> getAcademyCount(@PathVariable String month) {
        List<GetAcademyCountRes> result = managerService.getAcademyCount(month);
        return ResultResponse.<List<GetAcademyCountRes>>builder()
                .resultMessage("출력 완료")
                .resultData(result)
                .build();
    }

    @GetMapping("GetUserCount/{month}")
    @Operation(summary = "월별 등록한 유저 수", description = "이번달, 지난달 넣어서 테스트 해보시면 됩니다.")
    public ResultResponse<List<GetUserCountRes>> getUserCount(@PathVariable String month) {
        List<GetUserCountRes> result = managerService.getUserCount(month);
        return ResultResponse.<List<GetUserCountRes>>builder()
                .resultMessage("출력 완료")
                .resultData(result)
                .build();
    }

    @GetMapping("GetAcademyCostCount/{month}")
    @Operation(summary = "월별 결제한 건 수", description = "이번달, 지난달 넣어서 테스트 해보시면 됩니다.")
    public ResultResponse<List<GetAcademyCostCountRes>> getAcademyCostCount(@PathVariable String month) {
        List<GetAcademyCostCountRes> result = managerService.getAcademyCostCount(month);
        return ResultResponse.<List<GetAcademyCostCountRes>>builder()
                .resultMessage("출력 완료")
                .resultData(result)
                .build();
    }
}
package com.green.acamatch.manager;

import com.green.acamatch.config.model.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
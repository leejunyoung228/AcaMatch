package com.green.acamatch.accessLog;

import com.green.acamatch.config.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("access-log")
public class AccessLogController {
    private final AccessLogService accessLogService;

    @PostMapping
    @Operation(summary = "메인 화면 처음 접속시 요청 보내줘요")
    public ResultResponse<Integer> saveAccessLog(HttpServletRequest request) {
        return ResultResponse.<Integer>builder()
                .resultMessage("저장 성공")
                .resultData(accessLogService.saveLog(request))
                .build();
    }

    @GetMapping
    @Operation(summary = "하루간의 사용자 조회")
    public ResultResponse<Long> getAccessLog() {
        return ResultResponse.<Long>builder()
                .resultMessage("조회")
                .resultData(accessLogService.getUniqueAccessCount())
                .build();
    }
}

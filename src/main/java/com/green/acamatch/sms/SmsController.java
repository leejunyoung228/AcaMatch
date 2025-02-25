package com.green.acamatch.sms;

import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.config.security.AuthenticationFacade;
import com.green.acamatch.sms.model.SmsRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("sms")
@RequiredArgsConstructor
public class SmsController {
    private final SmsService smsService;
    private final UserMessage userMessage;

    @PostMapping("/send")
    @Operation(summary = "문자 전송", description = "사용자가 문자 메시지를 전송합니다.")
    public ResultResponse<Integer> sendSms(@RequestBody SmsRequest smsRequest) {
        // ✅ 현재 로그인된 사용자 ID 가져오기
        long requestUserId = AuthenticationFacade.getSignedUserId();

        // ✅ 특정 수업의 담당 선생님인지 검증 후 문자 전송
        int result = smsService.sendSms(smsRequest.getTo(), smsRequest.getText(), requestUserId, smsRequest.getClassId());

        return ResultResponse.<Integer>builder()
                .resultMessage(userMessage.getMessage()) // ✅ 서비스에서 설정된 메시지 사용
                .resultData(result) // ✅ 문자 전송 성공(1) / 실패(0)
                .build();
    }
}

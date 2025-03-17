package com.green.acamatch.sms;

import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.config.security.AuthenticationFacade;
import com.green.acamatch.sms.model.SmsRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("sms")
@RequiredArgsConstructor
public class SmsController {
    private final SmsService smsService;

    @GetMapping("/keys")
    @Operation(summary = "SMS API 키 가져오기")
    public ResultResponse<Map<String, String>> getSmsKeys() {
        Map<String, String> keys = smsService.getSmsKeys();
        return ResultResponse.<Map<String, String>>builder()
                .resultMessage("출력 성공")
                .resultData(keys)
                .build();
    }

//    @PostMapping("/send")
//    @Operation(summary = "문자 전송", description = "사용자가 문자 메시지를 전송합니다.")
//    public ResultResponse<Integer> sendSms(@RequestBody SmsRequest smsRequest) {
//        // 현재 로그인된 사용자 ID 가져오기
//        long requestUserId = AuthenticationFacade.getSignedUserId();
//
//        // 문자 전송 서비스 호출 및 응답 반환
//        return smsService.sendSms(smsRequest, requestUserId);
//    }
}


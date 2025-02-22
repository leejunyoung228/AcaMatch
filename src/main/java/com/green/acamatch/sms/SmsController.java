package com.green.acamatch.sms;

import com.green.acamatch.sms.model.SmsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SmsController {
    private final SmsService smsService;

    @PostMapping("/send")
    public String sendSms(@RequestBody SmsRequest smsRequest) {
        smsService.sendSms(smsRequest.getTo(), smsRequest.getText());
        return "문자 전송 완료!";
    }
}
package com.green.acamatch.sms;

import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.config.security.AuthenticationFacade;
import com.green.acamatch.sms.model.SmsRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("sms")
@RequiredArgsConstructor
public class SmsController {
    private final SmsService smsService;
    private final SmsConfigProperties smsConfigProperties;

    @GetMapping("/keys")
    @Operation(summary = "SMS API 키 가져오기")
    public ResultResponse<Map<String, String>> getSmsKeys() {
        Map<String, String> keys = smsService.getSmsKeys();
        return ResultResponse.<Map<String, String>>builder()
                .resultMessage("출력 성공")
                .resultData(keys)
                .build();
    }

    /**
     * Solapi HTTP API를 이용한 발신번호 등록
     */
    @PostMapping("/register-sender")
    @Operation(summary = "발신번호 등록 요청")
    public ResponseEntity<String> registerSenderNumber(@RequestParam String sender) {
        try {
            String response = smsService.registerSenderNumber(sender);
            return ResponseEntity.ok("발신번호 등록 요청 성공!\n응답: " + response);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body("발신번호 등록 실패: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("발신번호 등록 실패: " + e.getMessage());
        }
    }


    /**
     * Solapi SDK를 이용한 발신번호 인증 요청
     */
    @PostMapping("/send-verification")
    @Operation(summary = "발신번호 인증 요청")
    public ResultResponse<SingleMessageSentResponse> sendVerificationMessage(@RequestParam String sender) {
        try {
            SingleMessageSentResponse response = smsService.sendVerificationMessage(sender);
            return ResultResponse.<SingleMessageSentResponse>builder()
                    .resultMessage("발신번호 인증 요청 성공!")
                    .resultData(response)
                    .build();
        } catch (Exception e) {
            return ResultResponse.<SingleMessageSentResponse>builder()
                    .resultMessage("발신번호 인증 요청 실패: " + e.getMessage())
                    .resultData(null)
                    .build();
        }
    }

    @PostMapping("/send-single-logged-in")
    @Operation(summary = "로그인된 학원장의 발신번호로 개별 문자 발송")
    public ResultResponse<SingleMessageSentResponse> sendSingleMessageForLoggedInUser(
            @RequestParam String receiver,
            @RequestParam String text) {
        try {
            SingleMessageSentResponse response = smsService.sendSingleMessageForLoggedInUser(receiver, text);
            return ResultResponse.<SingleMessageSentResponse>builder()
                    .resultMessage("문자 발송 성공!")
                    .resultData(response)
                    .build();
        } catch (Exception e) {
            return ResultResponse.<SingleMessageSentResponse>builder()
                    .resultMessage("문자 발송 실패: " + e.getMessage())
                    .resultData(null)
                    .build();
        }
    }

    /**
     * 발신번호 등록 없이 문자 발송 테스트
     */
    @PostMapping("/send-test")
    @Operation(summary = "테스트 SMS 발송 (발신번호 등록 없이)")
    public ResponseEntity<SingleMessageSentResponse> sendTestSms(
            @RequestParam String receiver,
            @RequestParam String text) {
        try {
            SingleMessageSentResponse response = smsService.sendTestSms(receiver, text);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/send-to-class")
    @Operation(summary = "특정 학급 학생들에게 문자 발송")
    public ResultResponse<List<SingleMessageSentResponse>> sendBulkMessageForClass(
            @RequestParam Long classId,
            @RequestParam String textTemplate) {

        try {
            List<SingleMessageSentResponse> responses = smsService.sendBulkMessageForClass(classId, textTemplate);
            return ResultResponse.<List<SingleMessageSentResponse>>builder()
                    .resultMessage("문자 발송 성공!")
                    .resultData(responses)
                    .build();
        } catch (Exception e) {
            return ResultResponse.<List<SingleMessageSentResponse>>builder()
                    .resultMessage("문자 발송 실패: " + e.getMessage())
                    .resultData(null)
                    .build();
        }
    }


//    @PostMapping("/register-sender")
//    public ResponseEntity<String> registerSender(@RequestParam String sender) {
//        try {
//            smsConfigProperties.getSender() = new SenderNumber();
//            senderNumber.setPhoneNumber(sender);
//
//            smsService.addSenderNumber(senderNumber);
//            return ResponseEntity.ok("발신번호 등록 요청 완료!");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("등록 실패: " + e.getMessage());
//        }
//    }



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


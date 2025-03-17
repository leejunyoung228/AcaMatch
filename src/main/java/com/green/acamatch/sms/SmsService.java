package com.green.acamatch.sms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.acamatch.acaClass.AcaClassService;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.SmsErrorCode;
import com.green.acamatch.config.exception.UserErrorCode;
import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.sms.model.SmsRequest;
import com.green.acamatch.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.MultipleDetailMessageSentResponse;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService {

    private final SmsConfigProperties smsConfigProperties;
    private final DefaultMessageService messageService;
    private final UserRepository userRepository; // 학원장 계정 저장된 곳

    public Map<String, String> getSmsKeys() {
        Map<String, String> keys = new HashMap<>();
        keys.put("apiKey", smsConfigProperties.getApiKey());
        keys.put("apiSecret", smsConfigProperties.getApiSecret());
        return keys;
    }

    public String registerSenderNumber(String sender) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // ✅ Solapi HMAC 인증 정보 생성
        String apiKey = smsConfigProperties.getApiKey();
        String apiSecret = smsConfigProperties.getApiSecret();
        String timestamp = String.valueOf(System.currentTimeMillis());

        String signature = generateHmacSignature(timestamp, apiKey, apiSecret); // ✅ HMAC-SHA256 서명 생성

        // ✅ 올바른 인증 헤더 설정 (Solapi 공식 문서 기준)
        headers.set("Authorization", "HMAC-SHA256 " + apiKey + ":" + signature);
        headers.set("API-Key", apiKey);
        headers.set("Timestamp", timestamp);

        // ✅ 요청 Body 설정
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("phone", sender);
        requestBody.put("site_user", "your_site_user_id");  // ❗ 필수 파라미터

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://api.coolsms.co.kr/senderid/v1/sender",
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            return response.getBody();  // 📌 정상 응답 반환
        } catch (HttpClientErrorException e) {
            System.out.println("📌 Solapi API 오류 발생:");
            System.out.println("▶ 상태 코드: " + e.getStatusCode());
            System.out.println("▶ 응답 메시지: " + e.getResponseBodyAsString());
            throw new RuntimeException("발신번호 등록 실패: " + e.getResponseBodyAsString());
        }
    }



    // ✅ Solapi 공식 인증 방식 적용
    private String generateHmacSignature(String timestamp, String apiKey, String apiSecret) {
        try {
            String data = timestamp + apiKey;  // ✅ Solapi 요구 방식
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);  // ✅ Base64 인코딩 후 반환
        } catch (Exception e) {
            throw new RuntimeException("📌 HMAC Signature 생성 실패", e);
        }
    }


    /**
     * Solapi SDK를 이용한 발신번호 인증 요청
     */
    public SingleMessageSentResponse sendVerificationMessage(String sender) {
        Message message = new Message();
        message.setFrom(sender);
        message.setTo(sender);
        message.setText("[인증] 발신번호 등록을 위한 인증 요청");

        SingleMessageSendingRequest request = new SingleMessageSendingRequest(message);
        return messageService.sendOne(request);
    }

    /**
     * Solapi SDK를 이용한 개별 문자 발송 (발신번호 동적 설정)
     */
    public SingleMessageSentResponse sendSingleMessage(String sender, String receiver, String text) {
        Message message = new Message();
        message.setFrom(sender); // 사용자가 입력한 발신번호
        message.setTo(receiver);
        message.setText(text);

        SingleMessageSendingRequest request = new SingleMessageSendingRequest(message);
        return messageService.sendOne(request);
    }


    /**
     * 로그인된 학원장의 발신번호로 개별 문자 발송
     */
    public SingleMessageSentResponse sendSingleMessageForLoggedInUser(String receiver, String text) {
        // 현재 로그인된 사용자 가져오기
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("로그인 정보를 찾을 수 없습니다."));

        String sender = user.getPhone(); // 학원장의 발신번호

        Message message = new Message();
        message.setFrom(sender);
        message.setTo(receiver);
        message.setText(text);

        SingleMessageSendingRequest request = new SingleMessageSendingRequest(message);
        return messageService.sendOne(request);
    }

    /**
     * 발신번호 등록 없이 SMS 발송 (테스트용)
     */
    public SingleMessageSentResponse sendTestSms(String receiver, String text) {
        Message message = new Message();
        message.setFrom(smsConfigProperties.getSender()); // **내 번호 직접 입력 (발신번호 등록이 안 된 경우)**
        message.setTo(receiver);
        message.setText(text);

        SingleMessageSendingRequest request = new SingleMessageSendingRequest(message);
        return messageService.sendOne(request);
    }

//    private final DefaultMessageService messageService;
//    private final String sender;
//    private final UserMessage userMessage;
//    private final AcaClassService acaClassService;
//
//    public SmsService(@Value("${solapi.api-key}") String apiKey,
//                      @Value("${solapi.api-secret}") String apiSecret,
//                      @Value("${solapi.sender}") String sender,
//                      UserMessage userMessage,
//                      AcaClassService acaClassService) {
//        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.solapi.com");
//        this.sender = sender;
//        this.userMessage = userMessage;
//        this.acaClassService = acaClassService;
//    }
//
//    /**
//     * 문자 전송 처리 (서비스에서 모든 예외 처리 수행)
//     */
//    @Transactional
//    public ResultResponse<Integer> sendSms(SmsRequest smsRequest, long requestUserId) {
//        // 예외 처리 및 검증 수행 후 메시지 전송
//        try {
//            validateUser(requestUserId);
////            validateTeacherPermission(requestUserId, smsRequest.getClassId());
//            validateSmsParameters(smsRequest);
//            sendMessage(smsRequest, requestUserId);
//
//            return buildResponse(1, "문자 전송 성공!");
//        } catch (CustomException e) {
//            log.error(" 문자 전송 실패: {}", e.getMessage());
//            return buildResponse(0, e.getMessage());
//        } catch (Exception e) {
//            log.error(" 문자 전송 실패 (예상치 못한 오류): {}", e.getMessage(), e);
//            return buildResponse(0, "SMS 전송 중 예상치 못한 오류가 발생했습니다.");
//        }
//    }
//
//    /**
//     * 사용자 ID 검증
//     */
//    private void validateUser(long requestUserId) {
//        if (requestUserId <= 0) {
//            log.error("문자 전송 실패: 유효하지 않은 사용자 ID ({})", requestUserId);
//            throw new CustomException(UserErrorCode.USER_NOT_FOUND);
//        }
//    }
//
////    /**
////     * 특정 수업의 담당 선생님인지 확인
////     */
////    private void validateTeacherPermission(long requestUserId, long classId) {
////        acaClassService.validateTeacherPermission(requestUserId, classId);
////    }
//
//    /**
//     * SMS 전송을 위한 입력값 검증
//     */
//    private void validateSmsParameters(SmsRequest smsRequest) {
//        if (sender == null || sender.isEmpty()) {
//            log.error("문자 전송 실패: 발신자 정보가 없습니다.");
//            throw new CustomException(SmsErrorCode.INVALID_SENDER);
//        }
//        if (smsRequest.getTo() == null || smsRequest.getTo().isEmpty()) {
//            log.error("문자 전송 실패: 수신자 정보가 없습니다.");
//            throw new CustomException(SmsErrorCode.INVALID_RECEIVER);
//        }
//        if (smsRequest.getText() == null || smsRequest.getText().isEmpty()) {
//            log.error("문자 전송 실패: 메시지 내용이 없습니다.");
//            throw new CustomException(SmsErrorCode.EMPTY_MESSAGE);
//        }
//    }
//
//    /**
//     * Solapi API를 통한 메시지 전송
//     */
//    private void sendMessage(SmsRequest smsRequest, long requestUserId) {
//        Message message = new Message();
//        message.setFrom(sender);
//        message.setTo(smsRequest.getTo());
//        message.setText(smsRequest.getText());
//
//        SingleMessageSendingRequest request = new SingleMessageSendingRequest(message);
//
//        if (messageService == null) {
//            log.error("문자 전송 실패: messageService가 null입니다.");
//            throw new CustomException(SmsErrorCode.SERVICE_NOT_AVAILABLE);
//        }
//
//        try {
//            messageService.sendOne(request);
//            log.info("문자 전송 성공 (유저 ID: {}): {}", requestUserId, smsRequest.getText());
//        } catch (Exception e) {
//            log.error("문자 전송 실패 (API 오류): {}", e.getMessage(), e);
//            throw new CustomException(SmsErrorCode.SMS_SEND_FAILED);
//        }
//    }
//
//    /**
//     * 결과 응답 빌드
//     */
//    private ResultResponse<Integer> buildResponse(int result, String message) {
//        return ResultResponse.<Integer>builder()
//                .resultMessage(message)
//                .resultData(result)
//                .build();
//    }
}

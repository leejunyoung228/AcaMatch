package com.green.acamatch.sms;

import com.green.acamatch.acaClass.AcaClassService;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.SmsErrorCode;
import com.green.acamatch.config.exception.UserErrorCode;
import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.sms.model.SmsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SmsService {
    private final DefaultMessageService messageService;
    private final String sender;
    private final UserMessage userMessage;
    private final AcaClassService acaClassService;

    public SmsService(@Value("${solapi.api-key}") String apiKey,
                      @Value("${solapi.api-secret}") String apiSecret,
                      @Value("${solapi.sender}") String sender,
                      UserMessage userMessage,
                      AcaClassService acaClassService) {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.solapi.com");
        this.sender = sender;
        this.userMessage = userMessage;
        this.acaClassService = acaClassService;
    }

    /**
     * 문자 전송 처리 (서비스에서 모든 예외 처리 수행)
     */
    @Transactional
    public ResultResponse<Integer> sendSms(SmsRequest smsRequest, long requestUserId) {
        // 예외 처리 및 검증 수행 후 메시지 전송
        try {
            validateUser(requestUserId);
//            validateTeacherPermission(requestUserId, smsRequest.getClassId());
            validateSmsParameters(smsRequest);
            sendMessage(smsRequest, requestUserId);

            return buildResponse(1, "문자 전송 성공!");
        } catch (CustomException e) {
            log.error(" 문자 전송 실패: {}", e.getMessage());
            return buildResponse(0, e.getMessage());
        } catch (Exception e) {
            log.error(" 문자 전송 실패 (예상치 못한 오류): {}", e.getMessage(), e);
            return buildResponse(0, "SMS 전송 중 예상치 못한 오류가 발생했습니다.");
        }
    }

    /**
     * 사용자 ID 검증
     */
    private void validateUser(long requestUserId) {
        if (requestUserId <= 0) {
            log.error("문자 전송 실패: 유효하지 않은 사용자 ID ({})", requestUserId);
            throw new CustomException(UserErrorCode.USER_NOT_FOUND);
        }
    }

//    /**
//     * 특정 수업의 담당 선생님인지 확인
//     */
//    private void validateTeacherPermission(long requestUserId, long classId) {
//        acaClassService.validateTeacherPermission(requestUserId, classId);
//    }

    /**
     * SMS 전송을 위한 입력값 검증
     */
    private void validateSmsParameters(SmsRequest smsRequest) {
        if (sender == null || sender.isEmpty()) {
            log.error("문자 전송 실패: 발신자 정보가 없습니다.");
            throw new CustomException(SmsErrorCode.INVALID_SENDER);
        }
        if (smsRequest.getTo() == null || smsRequest.getTo().isEmpty()) {
            log.error("문자 전송 실패: 수신자 정보가 없습니다.");
            throw new CustomException(SmsErrorCode.INVALID_RECEIVER);
        }
        if (smsRequest.getText() == null || smsRequest.getText().isEmpty()) {
            log.error("문자 전송 실패: 메시지 내용이 없습니다.");
            throw new CustomException(SmsErrorCode.EMPTY_MESSAGE);
        }
    }

    /**
     * Solapi API를 통한 메시지 전송
     */
    private void sendMessage(SmsRequest smsRequest, long requestUserId) {
        Message message = new Message();
        message.setFrom(sender);
        message.setTo(smsRequest.getTo());
        message.setText(smsRequest.getText());

        SingleMessageSendingRequest request = new SingleMessageSendingRequest(message);

        if (messageService == null) {
            log.error("문자 전송 실패: messageService가 null입니다.");
            throw new CustomException(SmsErrorCode.SERVICE_NOT_AVAILABLE);
        }

        try {
            messageService.sendOne(request);
            log.info("문자 전송 성공 (유저 ID: {}): {}", requestUserId, smsRequest.getText());
        } catch (Exception e) {
            log.error("문자 전송 실패 (API 오류): {}", e.getMessage(), e);
            throw new CustomException(SmsErrorCode.SMS_SEND_FAILED);
        }
    }

    /**
     * 결과 응답 빌드
     */
    private ResultResponse<Integer> buildResponse(int result, String message) {
        return ResultResponse.<Integer>builder()
                .resultMessage(message)
                .resultData(result)
                .build();
    }
}

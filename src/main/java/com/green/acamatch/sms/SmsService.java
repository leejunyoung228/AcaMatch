package com.green.acamatch.sms;

import com.green.acamatch.acaClass.AcaClassService;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.UserMessage;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsService {
    private final DefaultMessageService messageService;
    private final String sender;
    private final UserMessage userMessage;
    private final AcaClassService  acaClassService;

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
     * ✅ 특정 수업의 담당 선생님만 문자 전송 가능하도록 검증 후 문자 전송
     */
    public int sendSms(String to, String text, long requestUserId, long classId) {
        try {
            // ✅ 특정 수업의 담당 선생님인지 검증
            acaClassService.validateTeacherPermission(requestUserId, classId);

            Message message = new Message();
            message.setFrom(sender);
            message.setTo(to);
            message.setText(text);

            SingleMessageSendingRequest request = new SingleMessageSendingRequest(message);
            messageService.sendOne(request);

            log.info("✅ 메시지 전송 성공 (유저 ID: {}): {}", requestUserId, text);
            userMessage.setMessage("문자 전송 성공!");
            return 1;
        } catch (CustomException e) {
            userMessage.setMessage(e.getMessage());
            log.error("❌ 문자 전송 실패: {}", e.getMessage());
            return 0;
        } catch (Exception e) {
            userMessage.setMessage("SMS 전송 중 예상치 못한 오류가 발생했습니다.");
            log.error("❌ 문자 전송 실패: {}", e.getMessage());
            return 0;
        }
    }
}

package com.green.acamatch.sms;


import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
    private final DefaultMessageService messageService;
    private final String sender;

    public SmsService(@Value("${solapi.api-key}") String apiKey,
                      @Value("${solapi.api-secret}") String apiSecret,
                      @Value("${solapi.sender}") String sender) {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.solapi.com");
        this.sender = sender;
    }

    public void sendSms(String to, String text) {
        Message message = new Message();
        message.setFrom(sender);
        message.setTo(to);
        message.setText(text);

        try {
            // ✅ 단일 메시지 요청 객체 생성
            SingleMessageSendingRequest request = new SingleMessageSendingRequest(message);

            // ✅ 단일 메시지 전송
            messageService.sendOne(request);

            System.out.println("✅ 메시지 전송 성공: " + text);
        } catch (Exception e) {
            System.out.println("❌ 메시지 전송 실패: " + e.getMessage());
        }
    }
}

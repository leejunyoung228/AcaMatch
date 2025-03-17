package com.green.acamatch.config;

import com.green.acamatch.sms.SmsConfigProperties;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(SmsConfigProperties.class)
public class SmsConfig {

    private final SmsConfigProperties smsConfigProperties;

    @Bean
    public DefaultMessageService messageService() {
        // 🔥 디버깅용: API Key 값 확인
        System.out.println("API Key: " + smsConfigProperties.getApiKey());
        System.out.println("API Secret: " + smsConfigProperties.getApiSecret());
        System.out.println("sender: " + smsConfigProperties.getSender());

        return NurigoApp.INSTANCE.initialize(
                smsConfigProperties.getApiKey(),
                smsConfigProperties.getApiSecret(),
                "https://api.coolsms.co.kr"
        );
    }
}

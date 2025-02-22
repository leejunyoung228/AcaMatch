package com.green.acamatch.config;

import com.green.acamatch.sms.SmsConfigProperties;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SmsConfig {

    private final SmsConfigProperties smsConfigProperties;

    @Bean
    public DefaultMessageService messageService() {
        return NurigoApp.INSTANCE.initialize(
                smsConfigProperties.getApiKey(),
                smsConfigProperties.getApiSecret(),
                "https://api.coolsms.co.kr"
        );
    }
}
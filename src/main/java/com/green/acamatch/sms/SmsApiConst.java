package com.green.acamatch.sms;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ToString
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "constant.solapi")
public class SmsApiConst {
    private final String apiKey;
    private final String apiSecret;
    private final String sender;
}

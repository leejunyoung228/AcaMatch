package com.green.acamatch.config.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ToString
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "constant.email-const")
public class EmailConst {
    private final String baseUrl;
    private final String signUpUrl;
    private final String tempPwUrl;
    private final String tokenKey;
    private final String pkKey;
    private final String fromEmail;
    private final String alias;
    private final String signUpTemplateName;
    private final String findPwTemplateName;
    private final long expiredTime;
    private final String signUpSubject;
    private final String findPwSubject;

}

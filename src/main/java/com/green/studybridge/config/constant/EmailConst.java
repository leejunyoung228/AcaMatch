package com.green.studybridge.config.constant;

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
    private final String url;
    private final String key;
    private final String templateName;
    private final long expiredTime;
    private final String subject;
}

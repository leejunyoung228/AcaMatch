package com.green.studybridge.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ToString
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "auth-const")
public class AuthConst {
    private String serverUrl;
}

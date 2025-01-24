package com.green.acamatch.config.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ToString
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "constant.user-const")
public class UserConst {
    private final String userPicFilePath;
    private final String redirectUrl;
}

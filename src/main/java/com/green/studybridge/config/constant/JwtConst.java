package com.green.studybridge.config.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "constant.jwt-const")
@RequiredArgsConstructor
@ToString
public class JwtConst {
    private final String issuer;
    private final String secret;
    private final String headerKey;
    private final String claimKey;
    private final String bearerFormat;
    private final String scheme;
    private final long accessTokenExpiry;
    private final long refreshTokenExpiry;
    private final String refreshTokenCookieName;
    private final int refreshTokenCookieExpiry;
}

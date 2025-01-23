package com.green.acamatch.config.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ToString
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "constant.post-address-const")
public class PostAddressApiConst {
    private final String baseUrl;
    private final String searchPubTransPathUrl;
    private final String paramApiKeyName;
    private final String paramApiKeyValue;
}

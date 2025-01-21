package com.green.acamatch.config.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "constant.info-const")
@RequiredArgsConstructor
@ToString
public class InfoConst {
    private final String title;
    private final String description;
    private final String version;
}

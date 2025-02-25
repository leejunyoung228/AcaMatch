package com.green.acamatch.config.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ToString
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "constant.academy-const")
public class AcademyConst {
    private final String academyPicFilePath;
    private final String businessLicenseFilePath;
    private final String operationLicenseFilePath;
}

package com.green.acamatch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod") // prod 환경에서만 활성화
public class FilePathConfig {

    @Value("${file.directory}")
    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }
}

package com.green.acamatch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FilePathConfig {

    @Value("${file.directory}")
    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }
}
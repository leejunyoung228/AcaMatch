package com.green.acamatch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"prod", "dev", "default"}) // 기본 프로파일도 포함!
public class FilePathConfig {

    @Value("${file.directory}")
    private String uploadDir;

    @Value("${excel.path}") // EXCEL_FILE_PATH 환경변수 매핑
    private String excelPath;

    public String getUploadDir() {
        return uploadDir;
    }

    public String getExcelPath() {
        return excelPath;
    }
}

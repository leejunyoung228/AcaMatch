package com.green.acamatch.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Getter
@Component
public class MyFileUtils {
    private final FilePathConfig filePathConfig;  // 주입받을 필드
    private final String uploadPath;

    // 생성자에서 `filePathConfig` 초기화
    public MyFileUtils(FilePathConfig filePathConfig) {
        this.filePathConfig = filePathConfig;
        this.uploadPath = filePathConfig.getUploadDir();
    }

    public String makeFolders(String path) {
        File file;

        // 절대경로인지 확인 후 설정 (Windows: C:\ or Linux: /)
        if (Paths.get(path).isAbsolute()) {
            file = new File(path);
        } else {
            file = new File(filePathConfig.getUploadDir(), path);
        }

        if (!file.exists() && !file.mkdirs()) {
            log.error("폴더 생성 실패: {}", file.getAbsolutePath());
            throw new RuntimeException("폴더 생성 실패: " + file.getAbsolutePath());
        }
        return file.getAbsolutePath();
    }

    public String getExt(String fileName) {
        int lastIdx = fileName.lastIndexOf(".");
        return fileName.substring(lastIdx);
    }

    public String makeRandomFileName() {
        return UUID.randomUUID().toString();
    }

    public String makeRandomFileName(MultipartFile mf) {
        return mf != null ? makeRandomFileName() + getExt(mf.getOriginalFilename()) : null;
    }

    public boolean deleteFolder(String path, boolean delRootFolder) {
        File dir = new File(uploadPath, path);

        // 디렉터리가 존재하고 폴더인지 확인
        if (dir.exists() && dir.isDirectory()) {
            File[] includedFiles = dir.listFiles();

            if (includedFiles != null) {
                for (File file : includedFiles) {
                    if (file.isDirectory()) {
                        // 하위 디렉터리를 재귀적으로 삭제
                        if (!deleteFolder(file.getPath(), true)) {
                            return false;
                        }
                    } else {
                        // 파일 삭제
                        if (!file.delete()) {
                            return false;
                        }
                    }
                }
            }

            // 최상위 폴더를 삭제할지 여부 확인
            if (delRootFolder) {
                return dir.delete();
            }
            return true; // 하위만 삭제하고 루트는 유지
        }
        return false; // 디렉터리가 존재하지 않거나 폴더가 아님
    }


    public void transferTo(MultipartFile mf, String relativePath) throws IOException {
        String baseDir = filePathConfig.getUploadDir();

        // 중복 방지: relativePath가 이미 절대 경로면 baseDir 붙이지 않음
        File file = new File(relativePath);
        if (!file.isAbsolute()) {
            file = new File(baseDir, relativePath);
        }

        String fullSavePath = file.getAbsolutePath();
        log.info(" 최종 파일 저장 경로: {}", fullSavePath);

        // 부모 디렉토리 확인 및 생성
        File parentDir = file.getParentFile();
        if (!parentDir.exists() && !parentDir.mkdirs()) {
            log.error("폴더 생성 실패: {}", parentDir.getAbsolutePath());
            throw new IOException("폴더 생성 실패: " + parentDir.getAbsolutePath());
        }

        // 파일 저장
        mf.transferTo(file);
    }


    public boolean deleteFile(String relativePath) {
        String baseDir = filePathConfig.getUploadDir(); // baseDir 가져오기
        String fullDeletePath = Paths.get(baseDir, relativePath).normalize().toString();
        File file = new File(fullDeletePath);

        if (file.exists() && file.isFile()) {
            boolean deleted = file.delete();
            log.info(deleted ? " 파일 삭제 성공: {}" : "파일 삭제 실패: {}", file.getAbsolutePath());
            return deleted;
        } else {
            log.warn(" 삭제할 파일이 존재하지 않음: {}", file.getAbsolutePath());
            return false;
        }
    }
}


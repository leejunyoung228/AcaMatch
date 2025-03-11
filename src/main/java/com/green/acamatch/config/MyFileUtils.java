package com.green.acamatch.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Getter
@Component
public class MyFileUtils {
    private final String uploadPath;

    public MyFileUtils(@Value("${file.directory}") String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String makeFolders(String path) {
        File file = new File(uploadPath, path);

        if (!file.exists()) {
            boolean isCreated = file.mkdirs(); // 폴더 생성
            if (!isCreated) {
                log.error("폴더 생성 실패: {}", file.getAbsolutePath());
                throw new RuntimeException("폴더 생성 실패: " + file.getAbsolutePath());
            }
        }

        if (!file.exists() || !file.isDirectory()) {
            log.error("폴더 생성 확인 실패: {}", file.getAbsolutePath());
            throw new RuntimeException("폴더 생성 확인 실패: " + file.getAbsolutePath());
        }

        log.info("폴더 생성 완료: {}", file.getAbsolutePath());
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


    public void transferTo(MultipartFile mf, String path) throws IOException {
        File file = new File(uploadPath, path);

        // 파일 저장 전 폴더 확인 및 생성
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                log.error("폴더 생성 실패: {}", parentDir.getAbsolutePath());
                throw new IOException("폴더 생성 실패: " + parentDir.getAbsolutePath());
            }
        }

        // 파일 저장
        mf.transferTo(file);
    }


    public boolean deleteFile(String path) {
        File file = new File(uploadPath, path);  // 경로와 파일명으로 File 객체 생성
        if (file.exists() && file.isFile()) {  // 파일이 존재하고 파일인지 확인
            boolean isDeleted = file.delete();  // 파일 삭제
            if (isDeleted) {
                log.info("File successfully deleted: {}", file.getAbsolutePath());
            } else {
                log.error("Failed to delete file: {}", file.getAbsolutePath());
            }
            return isDeleted;  // 삭제 성공 여부 반환
        } else {
            log.error("File does not exist or is not a file: {}", file.getAbsolutePath());
            return false;  // 파일이 존재하지 않거나 파일이 아니면 false 반환
        }
    }
}


package com.green.studybridge.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

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
            file.mkdirs();
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


    public void transferTo(MultipartFile mf, String path) throws IOException {
        File file = new File(uploadPath, path);
        mf.transferTo(file);
    }
}

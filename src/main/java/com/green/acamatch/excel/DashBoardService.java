package com.green.acamatch.excel;

import com.green.acamatch.academy.AcademyRepository;
import com.green.acamatch.academy.Service.AcademyService;
import com.green.acamatch.academy.model.HB.GetSearchInfoRes;
import com.green.acamatch.academyCost.model.GetAcademyCostInfoByMonth;
import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.config.exception.AcademyException;
import com.green.acamatch.config.exception.CommonErrorCode;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.excel.model.DashBoardDto;
import com.green.acamatch.manager.ManagerService;
import com.green.acamatch.manager.model.GetAcademyCostCountRes;
import com.green.acamatch.manager.model.GetAcademyCountRes;
import com.green.acamatch.manager.model.GetUserCountRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DashBoardService {
    private final ManagerService managerService;
    private MyFileUtils myFileUtils;
    private AcademyService academyService;

    @Value("${excel.path}")
    private String filePath;

    public String ExcelToDashBoard() {
        Path excelFilePath = Paths.get(filePath, "/dashBoard/dashBoard.xlsx");
        log.info("Excel file path: {}", excelFilePath);
        myFileUtils.makeFolders(excelFilePath.getParent().toString());
        try {
            Files.createDirectories(excelFilePath.getParent());
        } catch (IOException e) {
            log.error("디렉터리 생성 실패", e);
            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }

        return null;
    }
}
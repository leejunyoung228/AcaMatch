
package com.green.acamatch.excel;

import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.config.constant.EmailConst;
import com.green.acamatch.config.constant.UserConst;
import com.green.acamatch.config.exception.CommonErrorCode;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.excel.MariaDBConnection;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentGradeService {
    private final EmailConst emailConst;
    private final MyFileUtils myFileUtils;

    @Value("${file.directory}")  // application.yml의 excel.path 값 주입
    private String filePath;

    // 1. MariaDB에서 학생 성적 가져와 엑셀로 저장
    public String exportToExcel(long subjectId) { // subjectId를 매개변수로 추가
        Path excelFilePath = Paths.get(filePath, "/student_grades/studentGrade.xlsx");
        log.info("Excel file path: {}", excelFilePath);
        myFileUtils.makeFolders(excelFilePath.getParent().toString());
        try {
            Files.createDirectories(excelFilePath.getParent());
        } catch (IOException e) {
            log.error("디렉터리 생성 실패", e);
            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }

        String sql = "SELECT B.user_id, E.grade_id, A.`name`, D.subject_name, E.exam_date,\n" +
                "CASE WHEN D.SCORE_TYPE = 0 THEN E.score ELSE NULL END AS result_score,\n" +
                "CASE WHEN D.SCORE_TYPE != 0 THEN\n" +
                "CASE WHEN COALESCE(E.PASS, 0) = 0 THEN 0 ELSE 1 END ELSE NULL END AS result_pass\n" +
                "FROM `user` A\n" +
                "INNER JOIN joinclass B\n" +
                "ON A.user_id = B.user_id\n" +
                "INNER JOIN class C\n" +
                "ON B.class_id = C.class_id\n" +
                "INNER JOIN `subject` D\n" +
                "ON C.class_id = D.class_id\n" +
                "INNER JOIN grade E\n" +
                "ON B.join_class_id = E.join_class_id\n" +
                "WHERE E.subject_id = ?\n" +
                "GROUP BY user_id\n" +
                "ORDER BY A.user_id;";

        try (Connection conn = MariaDBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             Workbook workbook = new XSSFWorkbook()) {

            pstmt.setLong(1, subjectId);
            ResultSet rs = pstmt.executeQuery();
            Sheet sheet = workbook.createSheet("Student Grades");
            int rowIndex = 0;

            // 헤더 생성
            Row headerRow = sheet.createRow(rowIndex++);
            headerRow.createCell(0).setCellValue("User ID");
            headerRow.createCell(1).setCellValue("Grade ID");
            headerRow.createCell(2).setCellValue("Name");
            headerRow.createCell(3).setCellValue("Subject Name");
            headerRow.createCell(4).setCellValue("Exam Date");
            headerRow.createCell(5).setCellValue("Score");
            headerRow.createCell(6).setCellValue("Pass");

            // 데이터 추가
            while (rs.next()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(rs.getLong("user_id"));
                row.createCell(1).setCellValue(rs.getLong("grade_id"));
                row.createCell(2).setCellValue(rs.getString("name"));
                row.createCell(3).setCellValue(rs.getString("subject_name"));
                row.createCell(4).setCellValue(rs.getString("exam_date"));
                row.createCell(5).setCellValue(rs.getInt("result_score"));
                row.createCell(6).setCellValue(rs.getInt("result_pass"));
            }

//            Files.createDirectories(excelFilePath.getParent());
//            log.info("Excel file will be saved at: {}", excelFilePath.getParent());

            try (FileOutputStream fos = new FileOutputStream(excelFilePath.toString())) {
                workbook.write(fos);
                log.info("엑셀 파일 저장 경로: {}", excelFilePath);
                Resource fileResource  = new FileSystemResource(excelFilePath.toFile());
                String url = String.format("%s/xlsx/student_grades/studentGrade.xlsx", emailConst.getBaseUrl());
                return url;
            }

//            // 프론트엔드에 파일 경로 반환
//            Map<String, String> response = new HashMap<>();
//            response.put("filePath", excelFilePath.toString());
//            return ResponseEntity.ok(response);

//        } catch (SQLException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Collections.singletonMap("error", "데이터베이스 오류 발생"));
//
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Collections.singletonMap("error", "엑셀 파일 저장 오류 발생"));

//            response.setContentType("application/vnd.ms-excel");
//            response.setHeader("Content-Disposition", "attachment; filename=student_grades.xlsx");
//            workbook.write(response.getOutputStream());
//            response.getOutputStream().flush();

        } catch (Exception e) {
            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public ResultResponse<Integer> importFromExcel(MultipartFile file) {

        if (!file.getOriginalFilename().endsWith(".xlsx") && !file.getOriginalFilename().endsWith(".xls")) {
            return ResultResponse.<Integer>builder()
                    .resultMessage("엑셀 파일이 아닙니다. 올바른 파일을 선택해주세요.")
                    .resultData(0) // 실패를 나타내는 값 0
                    .build();
        }

        try (InputStream fis = file.getInputStream(); // 클라이언트가 업로드한 파일을 직접 읽음
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // 헤더는 건너뜀

                long userId = (long) row.getCell(0).getNumericCellValue();
                long gradeId = (long) row.getCell(1).getNumericCellValue();
                String name = row.getCell(2).getStringCellValue();
                String subjectName = row.getCell(3).getStringCellValue();
                String examDate = row.getCell(4).getStringCellValue();
                int score = (int) row.getCell(5).getNumericCellValue();
                int pass = (int) row.getCell(6).getNumericCellValue();

                // DB 업데이트
                updateStudentGrade(gradeId, score, pass);
            }

            return ResultResponse.<Integer>builder()
                    .resultMessage("DB에 수정을 성공하였습니다.")
                    .resultData(1)
                    .build();

        } catch (org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException e) {
            return ResultResponse.<Integer>builder()
                    .resultMessage("엑셀 파일이 아닙니다. 올바른 파일을 선택해주세요.")
                    .resultData(0) // 실패
                    .build();

        } catch (Exception e) {
            return ResultResponse.<Integer>builder()
                    .resultMessage("DB 수정 중 오류 발생: " + e.getMessage())
                    .resultData(0)
                    .build();
        }
    }

    // DB 업데이트 메서드 수정 (예외를 던지도록 변경)
    private void updateStudentGrade(long gradeId, int score, int pass) throws SQLException {
        String updateQuery = "UPDATE grade SET score = ?, pass = ? WHERE grade_id = ?;";

        try (Connection conn = MariaDBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            conn.setAutoCommit(false);

            pstmt.setInt(1, score);
            pstmt.setInt(2, pass);
            pstmt.setLong(3, gradeId);

            int rowsAffected = pstmt.executeUpdate();
            conn.commit();

            if (rowsAffected == 0) {
                throw new SQLException("업데이트 실패: 해당 grade_id(" + gradeId + ")가 존재하지 않습니다.");
            }

        } catch (SQLException e) {
            throw new SQLException("DB 업데이트 중 오류 발생: " + e.getMessage(), e);
        }
    }
}
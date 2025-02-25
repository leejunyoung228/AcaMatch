
package com.green.acamatch.excel;

import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.config.constant.EmailConst;
import com.green.acamatch.config.constant.UserConst;
import com.green.acamatch.config.exception.CommonErrorCode;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.excel.MariaDBConnection;
import com.green.acamatch.excel.model.StudentsGrade;
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
public class  StudentGradeService {
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

        String sql = "SELECT C.user_id, B.join_class_id, A.grade_id, C.name, D.subject_name, A.exam_date, D.score_type,\n" +
                "CASE WHEN D.SCORE_TYPE = 0 THEN A.score ELSE NULL END AS score,\n" +
                "CASE WHEN D.SCORE_TYPE != 0 THEN\n" +
                "CASE WHEN COALESCE(A.PASS, 0) = 0 THEN 0 ELSE 1 END ELSE NULL END AS pass\n" +
                "FROM grade A\n" +
                "INNER JOIN joinclass B\n" +
                "ON A.join_class_id = B.join_class_id\n" +
                "INNER JOIN `user` C\n" +
                "ON B.user_id = C.user_id\n" +
                "INNER JOIN `subject` D\n" +
                "ON A.subject_id = D.subject_id\n" +
                "INNER JOIN class E\n" +
                "ON D.class_id = E.class_id\n" +
                "WHERE D.subject_id = ?\n" +
                "GROUP BY C.user_id, A.grade_id, C.name, D.subject_name, A.exam_date, D.score_type, A.score, A.pass\n" +
                "ORDER BY C.user_id";

        try (Connection conn = MariaDBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             Workbook workbook = new XSSFWorkbook()) {

            pstmt.setLong(1, subjectId);
            ResultSet rs = pstmt.executeQuery();
            Sheet sheet = workbook.createSheet("Student Grades");
            int rowIndex = 0;
            rs.next();
            int scoreType = rs.getInt("score_type");
            // 헤더 생성
            Row headerRow = sheet.createRow(rowIndex++);
            headerRow.createCell(0).setCellValue("User ID");
            headerRow.createCell(1).setCellValue("joinClass ID");
            headerRow.createCell(2).setCellValue("Grade ID");
            headerRow.createCell(3).setCellValue("Name");
            headerRow.createCell(4).setCellValue("Subject Name");
            headerRow.createCell(5).setCellValue("Exam Date");
            headerRow.createCell(6).setCellValue(scoreType == 0 ? "Score" : "Pass - 0이면 불합격 / 1이면 합격");

            // 데이터 추가
            do {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(rs.getLong("user_id"));
                row.createCell(1).setCellValue(rs.getLong("join_class_id"));
                row.createCell(2).setCellValue(rs.getLong("grade_id"));
                row.createCell(3).setCellValue(rs.getString("name"));
                row.createCell(4).setCellValue(rs.getString("subject_name"));
                row.createCell(5).setCellValue(rs.getString("exam_date"));
                row.createCell(6).setCellValue(rs.getString(scoreType == 0 ? "score" : "pass"));
            } while (rs.next());

//            Files.createDirectories(excelFilePath.getParent());
//            log.info("Excel file will be saved at: {}", excelFilePath.getParent());

            try (FileOutputStream fos = new FileOutputStream(excelFilePath.toString())) {
                workbook.write(fos);
                log.info("엑셀 파일 저장 경로: {}", excelFilePath);
                Resource fileResource  = new FileSystemResource(excelFilePath.toFile());
                String url = String.format("%s/xlsx/student_grades/studentGrade.xlsx", emailConst.getBaseUrl());
                return url;
            }

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
            int scoreType = 0;

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    String type = row.getCell(6).getStringCellValue();
                    if (type.equals("Score")) {
                        scoreType = 0;
                    }else {
                        scoreType = 1;
                    }
                    // 헤더는 건너뜀
                    continue;
                }

                long userId = (long)row.getCell(1).getNumericCellValue();
                long joinClassId = (long)row.getCell(2).getNumericCellValue();
                long gradeId = (long) row.getCell(3).getNumericCellValue();
                String name = row.getCell(4).getStringCellValue();
                String subjectName = row.getCell(5).getStringCellValue();
                String examDate = row.getCell(6).getStringCellValue();
                Integer score = null;
                Integer pass = null;
                if (scoreType == 0) score = (int) row.getCell(7).getNumericCellValue();
                else pass = (int) row.getCell(7).getNumericCellValue();

                StudentsGrade studentsGrade = new StudentsGrade();
                // DB 업데이트
                InsertAndUpdateStudentGrade(studentsGrade);
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
    private void InsertAndUpdateStudentGrade(StudentsGrade p) throws SQLException {
        String query;
        if(p.getGradeId() != null && p.getGradeId() > 0) {
            if (p.getScore() != null) query = "UPDATE grade SET score = ? WHERE grade_id = ?;";
            else query = "UPDATE grade SET pass = ? WHERE grade_id = ?;";
        } else {
            query = "INSERT INTO grade\n" +
                    "        ( join_class_id, subject_id, score, pass, exam_date, processing_status )\n" +
                    "        SELECT join_class_id, ?, ?, ?, ?, ?\n" +
                    "        FROM joinclass\n" +
                    "        WHERE join_class_id = ?";
        }

        try (Connection conn = MariaDBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            conn.setAutoCommit(false);

            if(p.getGradeId() != null && p.getGradeId() > 0) {
                if (p.getScore() != null) pstmt.setInt(1, p.getScore());
                else pstmt.setInt(1, p.getPass());
                pstmt.setLong(2, p.getGradeId());
            }
            else {
                pstmt.setLong(1,p.getSubjectId());
                pstmt.setInt(2,p.getScore());
                pstmt.setInt(3,p.getPass());
                pstmt.setString(4,p.getExamDate());
                pstmt.setInt(5, p.getProcessingStatus());
                pstmt.setLong(6, p.getJoinClassId());
            }
            int rowsAffected = pstmt.executeUpdate();
            conn.commit();

            if (rowsAffected == 0) {
                throw new SQLException("저장 실패: 해당 grade_id(" + p.getGradeId() + ")에 대한 변경사항이 없습니다.");
            }

            if(p.getGradeId() == null && p.getGradeId() < 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long newGradeId = generatedKeys.getLong(1);
                    }
                }
            }

            if (rowsAffected == 0) {
                throw new SQLException("업데이트 실패: 해당 grade_id(" + p.getGradeId() + ")가 존재하지 않습니다.");
            }

        } catch (SQLException e) {
            throw new SQLException("DB 업데이트 중 오류 발생: " + e.getMessage(), e);
        }
    }
}
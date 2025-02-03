
package com.green.acamatch.excel;

import com.green.acamatch.excel.MariaDBConnection;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@Service
@RequiredArgsConstructor
public class StudentGradeService {

    // 1. MariaDB에서 학생 성적 가져와 엑셀로 저장
    public String exportToExcel(long subjectId) { // subjectId를 매개변수로 추가
        String filePath = "student_grades.xlsx";

        String sql = "SELECT A.user_id, A.`name`, D.subject_name, E.exam_date,\n" +
                "CASE WHEN D.SCORE_TYPE = 0 THEN E.score ELSE NULL END AS result_score,\n" +
                "CASE WHEN D.SCORE_TYPE != 0 THEN\n" +
                "CASE WHEN COALESCE(E.PASS, 0) = 0 THEN 0 ELSE 1 END ELSE NULL END AS result_pass\n" +
                "FROM `user` A\n" +
                "INNER JOIN joinclass B\n" +
                "ON A.user_id = B.user_id\n" +
                "INNER JOIN class C \n" +
                "ON B.class_id = C.class_id\n" +
                "INNER JOIN subject D\n" +
                "ON C.class_id = D.class_id\n" +
                "INNER JOIN grade E\n" +
                "ON B.join_class_id = E.join_class_id\n" +
                "WHERE D.subject_id = ?\n" +
                "GROUP BY A.user_id\n" +
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
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Subject Name");
            headerRow.createCell(3).setCellValue("Exam Date");
            headerRow.createCell(4).setCellValue("Score");
            headerRow.createCell(5).setCellValue("Pass");

            // 데이터 추가
            while (rs.next()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(rs.getLong("user_id"));
                row.createCell(1).setCellValue(rs.getString("name"));
                row.createCell(2).setCellValue(rs.getString("subject_name"));
                row.createCell(3).setCellValue(rs.getString("exam_date"));
                row.createCell(4).setCellValue(rs.getInt("result_score"));
                row.createCell(5).setCellValue(rs.getInt("result_pass"));
            }

            // 엑셀 파일 저장
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
                return "엑셀 파일 생성 성공: " + filePath;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "엑셀 파일 생성 실패: " + e.getMessage();
        }
    }

    public String importFromExcel(String filePath) {
        if (!filePath.endsWith(".xlsx") && !filePath.endsWith(".xls")) {
            return "엑셀 파일이 아닙니다. 올바른 파일을 선택해주세요.";
        }

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // 헤더는 건너뜀

                long gradeId = (long) row.getCell(0).getNumericCellValue();
                String name = row.getCell(1).getStringCellValue();
                String subjectName = row.getCell(2).getStringCellValue();
                String examDate = row.getCell(3).getStringCellValue();
                int score = (int) row.getCell(4).getNumericCellValue();
                int pass = (int) row.getCell(5).getNumericCellValue();

                // MariaDB에 업데이트
                updateStudentGrade(gradeId, score, pass);
            }

            return "DB에 수정을 성공하였습니다.";

        } catch (org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException e) {
            return "엑셀 파일이 아닙니다. 올바른 파일을 선택해주세요.";

        } catch (Exception e) {
            e.printStackTrace();
            return "DB에 수정을 실패하였습니다. " + e.getMessage();
        }
    }

    private void updateStudentGrade(long gradeId, int score, int pass) {
        String updateQuery = "UPDATE grade\n" +
                "SET  score = ?, pass = ? \n" +
                "WHERE grade_id = ?;";

        try (Connection conn = MariaDBConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            conn.setAutoCommit(false);

            pstmt.setLong(1, gradeId);
            pstmt.setInt(2, score);
            pstmt.setInt(3, pass);

            pstmt.executeUpdate();
            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

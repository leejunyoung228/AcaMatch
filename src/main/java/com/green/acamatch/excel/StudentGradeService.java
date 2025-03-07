
package com.green.acamatch.excel;

import com.green.acamatch.acaClass.ClassRepository;
import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.config.constant.EmailConst;
import com.green.acamatch.config.exception.AcaClassErrorCode;
import com.green.acamatch.config.exception.CommonErrorCode;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.UserErrorCode;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.entity.acaClass.AcaClass;
import com.green.acamatch.entity.exam.Exam;
import com.green.acamatch.entity.grade.Grade;
import com.green.acamatch.entity.joinClass.JoinClass;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.exam.ExamRepository;
import com.green.acamatch.excel.model.StudentsGradeDto;
import com.green.acamatch.grade.GradeRepository;
import com.green.acamatch.joinClass.JoinClassRepository;
import com.green.acamatch.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentGradeService {
    private final GradeRepository gradeRepository;
    private final MyFileUtils myFileUtils;
    private final JoinClassRepository joinClassRepository;
    private final ClassRepository classRepository;
    private final UserRepository userRepository;
    private final ExamRepository examRepository;
    private final EmailConst emailConst;

    @Value("${file.directory}")
    private String filePath;

    // 1. MariaDB에서 학생 성적 가져와 엑셀로 저장
    public String exportToExcel(Long examId) {
        Path excelFilePath = Paths.get(filePath, "student_grades/studentGrade.xlsx");
        log.info("Excel file path: {}", excelFilePath);
        myFileUtils.makeFolders(excelFilePath.getParent().toString());

        try {
            Files.createDirectories(excelFilePath.getParent());
        } catch (IOException e) {
            log.error("디렉터리 생성 실패", e);
            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }

        List<Object[]> result = gradeRepository.findExamGradeByExamId(examId);
        List<StudentsGradeDto> studentsGradeDtoList = result.stream().map(row -> {
            AcaClass acaClass = (AcaClass) row[0];
            Integer score = row[8] != null ? ((Number) row[8]).intValue() : 0;
            Integer pass = row[9] != null ? ((Number) row[9]).intValue() : 0;
            Integer processingStatus = row[10] != null ? ((Number) row[10]).intValue() : 0;
            return new StudentsGradeDto(
                    acaClass.getClassId(), //classId
                    ((Number) row[1]).longValue(), //userId
                    (String) row[2], //name
                    ((Number) row[3]).longValue(), //examId
                    (String) row[4], //examName
                    (row[5] instanceof java.sql.Date) ? ((java.sql.Date) row[5]).toLocalDate() : (LocalDate) row[5], //examDate
                    row[6] != null ? ((Number) row[6]).longValue() : null, //gradeId
                    ((Number) row[7]).intValue(), //examType
                    score, pass, processingStatus
            );
        }).toList();

        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(excelFilePath.toString())) {

            Sheet sheet = workbook.createSheet("Student Grades");
            int rowIndex = 0;

            // 헤더 생성
            Row headerRow = sheet.createRow(rowIndex++);
            headerRow.createCell(0).setCellValue("Class ID");
            headerRow.createCell(1).setCellValue("User ID");
            headerRow.createCell(2).setCellValue("Name");
            headerRow.createCell(3).setCellValue("Exam ID");
            headerRow.createCell(4).setCellValue("Exam Name");
            headerRow.createCell(5).setCellValue("Exam Date");
            headerRow.createCell(6).setCellValue("Grade ID");
            headerRow.createCell(7).setCellValue("Exam Type");
            headerRow.createCell(8).setCellValue(studentsGradeDtoList.get(0).getExamType() == 0 ? "Score" : "Pass - 0이면 불합격 / 1이면 합격");
            headerRow.createCell(9).setCellValue("Processing Status");

            // 데이터 추가
            for (StudentsGradeDto grade : studentsGradeDtoList) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(grade.getClassId());
                row.createCell(1).setCellValue(grade.getUserId());
                row.createCell(2).setCellValue(grade.getName());
                row.createCell(3).setCellValue(grade.getExamId());
                row.createCell(4).setCellValue(grade.getExamName());
                if (grade.getExamDate() != null) {
                    row.createCell(5).setCellValue(grade.getExamDate().toString());
                } else {
                    row.createCell(5).setCellValue("");
                }
                if (grade.getGradeId() != null) {
                    row.createCell(6).setCellValue(grade.getGradeId());
                } else {
                    row.createCell(6).setCellValue("");
                }
                row.createCell(7).setCellValue(grade.getExamType());
                row.createCell(8).setCellValue(grade.getExamType() == 0 ? grade.getScore() : grade.getPass());
                row.createCell(9).setCellValue(grade.getProcessingStatus());
            }

            workbook.write(fos);
            log.info("엑셀 파일 저장 경로 : {}", excelFilePath);

            return String.format("%s/xlsx/student_grades/studentGrade.xlsx", emailConst.getBaseUrl());

        } catch (Exception e) {
            log.error("엑셀 파일 생성 중 오류 발생", e);
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public ResultResponse<Integer> importFromExcel(MultipartFile file) {

        if (!file.getOriginalFilename().endsWith(".xlsx") && !file.getOriginalFilename().endsWith(".xls")) {
            return ResultResponse.<Integer>builder()
                    .resultMessage("엑셀 파일이 아닙니다. 올바른 파일을 선택해주세요.")
                    .resultData(0)
                    .build();
        }

        try (InputStream fis = file.getInputStream(); // 클라이언트가 업로드한 파일을 직접 읽음
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int scoreType = 0;
            List<Grade> gradeList = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    // 첫 번째 행은 헤더로 스킵
                    String type = row.getCell(7) != null ? row.getCell(7).getStringCellValue() : "";
                    scoreType = type.equals("Score") ? 0 : 1;
                    continue;
                }

                // 셀 값 처리
                long classId = getCellValueAsNumeric(row.getCell(1));
                long userId = getCellValueAsNumeric(row.getCell(2));
                String name = getCellValueAsString(row.getCell(3));
                long examId = getCellValueAsNumeric(row.getCell(4));
                String examName = getCellValueAsString(row.getCell(5));
                LocalDate examDate = getCellValueAsDate(row.getCell(6));
                Long gradeId = getCellValueAsNumeric(row.getCell(7));
                long examType = (int)getCellValueAsNumeric(row.getCell(8));
                Integer score = null;
                Integer pass = null;
                // score와 pass 처리
                if (examType == 0 && row.getCell(8) != null) {
                    score = (int) getCellValueAsNumeric(row.getCell(9)); // Numeric 값 처리
                } else if (examType == 1 && row.getCell(8) != null) {
                    pass = (int) getCellValueAsNumeric(row.getCell(9)); // Numeric 값 처리
                }
                long processingStatus = (int)getCellValueAsNumeric(row.getCell(10));

                Exam exam = examRepository.findById(examId).orElseThrow(()
                        -> new CustomException(AcaClassErrorCode.NOT_FOUND_EXAM));

                Grade grade = gradeRepository.findById(gradeId).orElse(new Grade());
                grade.setExam(exam);
                grade.setScore(score);
                grade.setPass(pass);
                grade.setExamDate(examDate);
                grade.setProcessingStatus((int)processingStatus);

                gradeList.add(grade);
            }

            gradeRepository.saveAll(gradeList);


            return ResultResponse.<Integer>builder()
                    .resultMessage("DB에 수정을 성공하였습니다.")
                    .resultData(1)
                    .build();

        } catch (org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException e) {
            return ResultResponse.<Integer>builder()
                    .resultMessage("엑셀 파일이 아닙니다. 올바른 파일을 선택해주세요.")
                    .resultData(0)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResultResponse.<Integer>builder()
                    .resultMessage("DB 수정 중 오류 발생: " + e.getMessage())
                    .resultData(0)
                    .build();
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell != null) {
            if (cell.getCellType() == CellType.STRING) {
                return cell.getStringCellValue();
            } else if (cell.getCellType() == CellType.NUMERIC) {
                return String.valueOf(cell.getNumericCellValue());
            }
        }
        return "";
    }

    private long getCellValueAsNumeric(Cell cell) {
        if (cell != null && cell.getCellType() == CellType.NUMERIC) {
            return (long) cell.getNumericCellValue();
        }
        return 0L;  // 기본값 처리
    }

    private LocalDate getCellValueAsDate(Cell cell) {
        if (cell != null && cell.getCellType() == CellType.NUMERIC) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }
        return null;
    }



//    private void InsertAndUpdateStudentGrade(StudentsGradeDto p) throws SQLException {
//        String query;
//        if (p.getGradeId() != null && p.getGradeId() > 0) {
//            if (p.getScore() != null) query = "UPDATE grade SET score = ? WHERE grade_id = ?;";
//            else query = "UPDATE grade SET pass = ? WHERE grade_id = ?;";
//        } else {
//            query = "INSERT INTO grade\n" +
//                    "        ( join_class_id, subject_id, score, pass, exam_date, processing_status )\n" +
//                    "        SELECT join_class_id, ?, ?, ?, ?, ?\n" +
//                    "        FROM joinclass\n" +
//                    "        WHERE join_class_id = ?";
//        }
//
//        try (Connection conn = MariaDBConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(query)) {
//
//            conn.setAutoCommit(false);
//
//            if (p.getGradeId() != null && p.getGradeId() > 0) {
//                if (p.getScore() != null) pstmt.setInt(1, p.getScore());
//                else pstmt.setInt(1, p.getPass());
//                pstmt.setLong(2, p.getGradeId());
//            } else {
//                pstmt.setLong(1, p.getSubjectId());
//                pstmt.setInt(2, p.getScore());
//                pstmt.setInt(3, p.getPass());
//                pstmt.setString(4, p.getExamDate());
//                pstmt.setInt(5, p.getProcessingStatus());
//                pstmt.setLong(6, p.getJoinClassId());
//            }
//            int rowsAffected = pstmt.executeUpdate();
//            conn.commit();
//
//            if (rowsAffected == 0) {
//                throw new SQLException("저장 실패: 해당 grade_id(" + p.getGradeId() + ")에 대한 변경사항이 없습니다.");
//            }
//
//            if (p.getGradeId() == null && p.getGradeId() < 0) {
//                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
//                    if (generatedKeys.next()) {
//                        long newGradeId = generatedKeys.getLong(1);
//                    }
//                }
//            }
//
//            if (rowsAffected == 0) {
//                throw new SQLException("업데이트 실패: 해당 grade_id(" + p.getGradeId() + ")가 존재하지 않습니다.");
//            }
//
//        } catch (SQLException e) {
//            throw new SQLException("DB 업데이트 중 오류 발생: " + e.getMessage(), e);
//        }
//    }
}
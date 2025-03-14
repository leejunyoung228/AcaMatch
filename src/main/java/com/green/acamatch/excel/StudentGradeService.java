
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    private final ExamRepository examRepository;
    private final EmailConst emailConst;
    @Value("${excel.path}")
    private String filePath;
    // 1. MariaDB에서 학생 성적 가져와 엑셀로 저장
    public String exportToExcel(Long examId) {
        // 경로 설정 (OS 호환성 고려)
        Path excelDirectory = Paths.get(filePath, "student_grades");
        Path excelFilePath = excelDirectory.resolve("studentGrade.xlsx").toAbsolutePath();
        log.info("Excel 파일 저장 경로: {}", excelFilePath);
        // 폴더 생성 (절대경로 문제 해결)
        myFileUtils.makeFolders(excelDirectory.toString());
        try {
            Files.createDirectories(excelDirectory);
        } catch (IOException e) {
            log.error("디렉터리 생성 실패: {}", excelDirectory, e);
            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
        // 파일이 존재하는지 확인하고 이름을 변경
        File excelFile = excelFilePath.toFile();
        if (excelFile.exists()) {
            int counter = 1;
            String newFileName;
            // 파일 이름에 (1), (2) 등을 붙여서 새로운 파일 경로 설정
            do {
                newFileName = String.format("studentGrade(%d).xlsx", counter);
                excelFilePath = Paths.get(filePath, "student_grades", newFileName);
                excelFile = excelFilePath.toFile();
                counter++;
            } while (excelFile.exists()); // 파일이 존재하는 동안 계속 이름을 바꾼다.
        }
        List<Object[]> result = gradeRepository.findExamGradeByExamId(examId);
        List<StudentsGradeDto> studentsGradeDtoList = result.stream().map(row -> {
            Integer score = (row[7] instanceof Number) ? ((Number) row[7]).intValue() : null;
            Integer pass = (row[8] instanceof Number) ? ((Number) row[8]).intValue() : null;
            Integer processingStatus = (row[9] instanceof Number) ? ((Number) row[9]).intValue() : 0;
            return new StudentsGradeDto(
                    ((Number) row[0]).longValue(), // joinClassID
                    (String) row[1], // name
                    ((Number) row[2]).longValue(), // examId
                    (String) row[3], // examName
                    (row[4] instanceof java.sql.Date) ? ((java.sql.Date) row[4]).toLocalDate() : (LocalDate) row[4], // examDate
                    row[5] != null ? ((Number) row[5]).longValue() : null, // gradeId
                    row[6] != null ? ((Number) row[6]).intValue() : null, // examType
                    score, pass, processingStatus
            );
        }).toList();
        if (studentsGradeDtoList.isEmpty()) {
            log.warn("학생 성적 데이터가 없습니다. 엑셀을 생성하지 않습니다.");
            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(excelFilePath.toString())) {
            Sheet sheet = workbook.createSheet("Student Grades");
            int rowIndex = 0;
            // 헤더 생성
            String[] headers = {"수강생 ID", "이름", "시험 ID", "시험 이름", "시험 날짜",
                    "성적 ID", "성적 유형", "점수", "통과 여부", "처리 상태"};
            Row headerRow = sheet.createRow(rowIndex++);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }
            // 데이터 추가
            for (StudentsGradeDto grade : studentsGradeDtoList) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(grade.getJoinClassId());
                row.createCell(1).setCellValue(grade.getName());
                row.createCell(2).setCellValue(grade.getExamId());
                row.createCell(3).setCellValue(grade.getExamName());
                row.createCell(4).setCellValue(grade.getExamDate() != null ? grade.getExamDate().toString() : "");
                row.createCell(5).setCellValue(grade.getGradeId() != null ? grade.getGradeId().toString() : "");
                row.createCell(6).setCellValue(grade.getExamType());
                if (grade.getExamType() == 0) {
                    row.createCell(7).setCellValue(grade.getScore() != null ? grade.getScore() : 0);
                } else {
                    row.createCell(8).setCellValue(grade.getPass() != null ? grade.getPass() : 0);
                }
                row.createCell(9).setCellValue(grade.getProcessingStatus() != null ? grade.getProcessingStatus() : 0);
            }
            workbook.write(fos);
            log.info("엑셀 파일 생성 완료: {}", excelFilePath);
            // URL 반환 (파일 경로가 아닌 API로 접근할 수 있도록)
            String url = String.format("%s/xlsx/student_grades/studentGrade.xlsx", emailConst.getBaseUrl());
            return url;
        } catch (Exception e) {
            log.error("엑셀 파일 생성 중 오류 발생", e);
            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional
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
            List<Grade> gradeList = new ArrayList<>();
            for (Row row : sheet) {
                if (row.getRowNum() == 0) { // 첫 번째 행은 헤더로 스킵
                    continue;
                }
                long joinClassId = getCellValue(row.getCell(0));
                String name = null;
                if (row.getCell(1).getCellType() == CellType.STRING) {
                    name = row.getCell(1).getStringCellValue();
                } else if (row.getCell(1).getCellType() == CellType.NUMERIC) {
                    name = String.valueOf(row.getCell(1).getNumericCellValue());
                } else {
                    name = "";
                }
                Long examId = getCellValue(row.getCell(2));
                String examName = null;
                if (row.getCell(3).getCellType() == CellType.STRING) {
                    examName = row.getCell(3).getStringCellValue();
                } else if (row.getCell(3).getCellType() == CellType.NUMERIC) {
                    examName = String.valueOf(row.getCell(3).getNumericCellValue());
                } else {
                    examName = "";
                }
                LocalDate examDate = null;
                if (row.getCell(4).getCellType() == CellType.STRING) {
                    String dateString = row.getCell(4).getStringCellValue().trim();
                    if (!dateString.isEmpty()) {
                        examDate = LocalDate.parse(dateString); // 문자열을 LocalDate로 변환
                    }
                } else if (row.getCell(4).getCellType() == CellType.NUMERIC) {
                    // NUMERIC 셀의 날짜 값을 LocalDate로 변환
                    examDate = row.getCell(4).getLocalDateTimeCellValue().toLocalDate(); // Excel에서 날짜는 LocalDateTime으로 처리됨
                }
                long gradeId = getCellValue(row.getCell(5));
                long examType = getCellValue(row.getCell(6));
                Integer score = null;
                Integer pass = null;
                if (examType == 0) {
                    if (row.getCell(7).getCellType() == CellType.NUMERIC) {
                        score = (int) row.getCell(7).getNumericCellValue();
                    } else if (row.getCell(7).getCellType() == CellType.STRING) {
                        try {
                            score = Integer.parseInt(row.getCell(7).getStringCellValue().trim());
                        } catch (NumberFormatException e) {
                            score = null;
                        }
                    }
                } else {
                    if (row.getCell(8).getCellType() == CellType.NUMERIC) {
                        pass = (int) row.getCell(8).getNumericCellValue();
                    } else if (row.getCell(8).getCellType() == CellType.STRING) {
                        try {
                            pass = Integer.parseInt(row.getCell(8).getStringCellValue().trim());
                        } catch (NumberFormatException e) {
                            pass = null;
                        }
                    }
                }
//                // `pass`가 null일 경우 기본값 설정
//                if (pass == null) {
//                    pass = 0;  // 기본값을 설정 (0으로 설정 가능)
//                }
                Cell cell = row.getCell(9);
                Integer processingStatus = null;
                if (cell != null) { // 셀이 null인지 확인
                    if (cell.getCellType() == CellType.NUMERIC) {
                        processingStatus = (int) cell.getNumericCellValue(); // 숫자형 셀 처리
                    } else if (cell.getCellType() == CellType.STRING) {
                        try {
                            processingStatus = Integer.parseInt(cell.getStringCellValue().trim()); // 문자열 셀 처리
                        } catch (NumberFormatException e) {
                            processingStatus = null; // 숫자가 아니면 null 처리
                        }
                    }
                } else {
                    processingStatus = 0;
                }
                // 데이터베이스에 저장
                JoinClass joinClass = joinClassRepository.findById(joinClassId).orElseThrow(()
                        -> new CustomException(AcaClassErrorCode.NOT_FOUND_JOIN_CLASS));
                Exam exam = examRepository.findById(examId)
                        .orElseThrow(() -> {
                            log.error("Exam not found for examId: {}", examId);
                            return new CustomException(AcaClassErrorCode.NOT_FOUND_EXAM);
                        });
                Grade grade = gradeRepository.findById(gradeId).orElse(new Grade());
                grade.setJoinClass(joinClass);
                grade.setExam(exam);
                grade.setExamDate(examDate);
                grade.setScore(score);
                grade.setPass(pass);
                grade.setProcessingStatus(processingStatus);
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
    public Long getCellValue(Cell cell) {
        if (cell == null) {
            return 0L; // 셀이 null이면 기본값 0 반환
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                return (long) cell.getNumericCellValue(); // 숫자인 경우 변환
            case STRING:
                try {
                    return Long.parseLong(cell.getStringCellValue().trim()); // 문자열을 숫자로 변환
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format: " + cell.getStringCellValue());
                    return 0L; // 변환 실패 시 0 반환
                }
            default:
                return 0L; // 기타 경우 기본값 0 반환
        }
    }
}
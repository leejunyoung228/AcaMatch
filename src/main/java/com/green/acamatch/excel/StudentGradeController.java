package com.green.acamatch.excel;

import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "엑셀 관리", description = "엑셀로 내보내기, 수정")
@RestController
@RequiredArgsConstructor
@RequestMapping("/grade")
public class StudentGradeController {
    private final StudentGradeService studentGradeService;
    private final UserMessage userMessage;

    // 1. 성적 엑셀 파일로 내보내기 (GET 요청)
    @GetMapping("/export")
    @Operation(summary = "엑셀 파일로 내보내기")
    public ResultResponse<String> exportToExcel(@Parameter(name = "subjectId", required = true, in = ParameterIn.QUERY)
                                                @RequestParam long subjectId, HttpServletResponse response) {
        String result = studentGradeService.exportToExcel(subjectId, response);
        boolean isSuccess = !result.startsWith("엑셀 파일 저장 실패");
        return ResultResponse.<String>builder()
                .resultMessage(isSuccess ? "엑셀 파일 내보내기에 성공하였습니다." : "엑셀 파일 내보내기에 실패하였습니다.")
                .resultData(result)
                .build();
    }

    // 2. 성적 엑셀 파일을 읽어 DB 업데이트 (POST 요청)
    @PostMapping(value = "/import", consumes = "multipart/form-data")
    @Operation(summary = "엑셀 파일을 읽어 DB 업데이트")
    public ResultResponse<Integer> importGrades(@RequestParam("file") MultipartFile file) {
        String result = studentGradeService.importFromExcel(file);
        boolean isSuccess = !result.startsWith("DB에 저장 실패");
        return ResultResponse.<Integer>builder()
                .resultMessage(isSuccess ? "DB 업데이트에 성공하였습니다." : "DB 업데이트에 실패하였습니다.")
                .resultData(result.contains("성공") ? 1 : 0)
                .build();
    }
}
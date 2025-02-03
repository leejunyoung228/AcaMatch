package com.green.acamatch.excel;

import com.green.acamatch.config.exception.UserMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public String exportToExcel(@Parameter(name = "subjectId", required = true, in = ParameterIn.QUERY)
                                                @RequestParam long subjectId){
        return studentGradeService.exportToExcel(subjectId);
    }

    // 2. 성적 엑셀 파일을 읽어 DB 업데이트 (POST 요청)
    @PostMapping("/import")
    @Operation(summary = "엑셀 파일을 읽어 DB 업데이트")
    public String importGrades(@RequestParam String filePath) {
        return studentGradeService.importFromExcel(filePath);
    }
}
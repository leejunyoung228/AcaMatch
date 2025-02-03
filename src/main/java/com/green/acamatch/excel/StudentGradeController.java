package com.green.acamatch.excel;

import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.model.ResultResponse;
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
    public ResultResponse<Integer> exportToExcel(@Parameter(name = "subjectId", required = true, in = ParameterIn.QUERY)
                                                @RequestParam long subjectId){
        String result = studentGradeService.exportToExcel(subjectId);
        int resultData = result.contains("성공") ? 1 : 0;
        return ResultResponse.<Integer>builder()
                .resultMessage("엑셀 파일 내보내기에 성공하였습니다.")
                .resultData(resultData)
                .build();
    }

    // 2. 성적 엑셀 파일을 읽어 DB 업데이트 (POST 요청)
    @PostMapping("/import")
    @Operation(summary = "엑셀 파일을 읽어 DB 업데이트")
    public ResultResponse<Integer> importGrades(@RequestParam String filePath) {
        String result = studentGradeService.importFromExcel(filePath);
        int resultData = result.contains("성공") ? 1 : 0;
        return ResultResponse.<Integer>builder()
                .resultMessage("DB 업데이트에 성공하였습니다.")
                .resultData(resultData)
                .build();
    }
}
package com.green.studybridge.grade;

import com.green.studybridge.config.model.ResultResponse;
import com.green.studybridge.grade.model.GradePostReq;
import com.green.studybridge.grade.model.GradeGetDto;
import com.green.studybridge.grade.model.GradeGetReq;
import com.green.studybridge.grade.model.GradePutReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "성적 관리", description = "성적 등록")
@RestController
@RequiredArgsConstructor
@RequestMapping("grade")
public class GradeController {
    private final GradeService service;

    @PostMapping
    @Operation(summary = "시험 점수 등록하기")
    public ResultResponse<Integer> postGrade(@RequestBody GradePostReq p) {
        Integer result = service.postGrade(p);
        return ResultResponse.<Integer>builder()
                .resultMessage("성적 등록 완료")
                .resultData(result)
                .build();
    }

    @GetMapping
    @Operation(summary = "시험 점수 가져오기")
    public ResultResponse<List<GradeGetDto>> selGradeScore(@ModelAttribute @ParameterObject GradeGetReq p) {
        List<GradeGetDto> result = service.selGradeScore(p);
        return ResultResponse.<List<GradeGetDto>>builder()
                .resultMessage("시험 점수 출력 완료")
                .resultData(result)
                .build();
    }

    @PutMapping
    @Operation(summary = "시험 점수 수정하기")
    public ResultResponse<Integer> updGradeScore(@RequestBody GradePutReq p) {
        Integer result = service.updGradeScore(p);
        return ResultResponse.<Integer>builder()
                .resultMessage("성적 수정 완료")
                .resultData(result)
                .build();
    }
}
package com.green.acamatch.grade;

import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.grade.model.GradePostReq;
import com.green.acamatch.grade.model.GradeGetDto;
import com.green.acamatch.grade.model.GradeGetReq;
import com.green.acamatch.grade.model.GradePutReq;
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
    private final UserMessage userMessage;

    @PostMapping
    @Operation(summary = "시험 점수 등록하기/ joinclass 값을 입력한 후 성적 입력 가능")
    public ResultResponse<Integer> postGrade(@RequestBody GradePostReq p) {
        try {
            Integer result = service.postGrade(p);
            return ResultResponse.<Integer>builder()
                    .resultMessage(userMessage.getMessage())
                    .resultData(result)
                    .build();
        } catch (IllegalArgumentException e) {
            return ResultResponse.<Integer>builder()
                    .resultMessage(e.getMessage())
                    .resultData(0)
                    .build();
        }
    }

    @GetMapping
    @Operation(summary = "시험 점수 가져오기/ null일 경우 resultData 반환 값이 없습니다.")
    public ResultResponse<List<GradeGetDto>> selGradeScore(@ModelAttribute @ParameterObject GradeGetReq p) {
        List<GradeGetDto> result = service.selGradeScore(p);
        return ResultResponse.<List<GradeGetDto>>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(result)
                .build();
    }

    @PutMapping
    @Operation(summary = "시험 점수 수정하기")
    public ResultResponse<Integer> updGradeScore(@RequestBody GradePutReq p) {
        Integer result = service.updGradeScore(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(result)
                .build();
    }
}
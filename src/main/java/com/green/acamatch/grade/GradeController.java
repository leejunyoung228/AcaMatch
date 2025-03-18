package com.green.acamatch.grade;

import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.grade.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
            Integer result = service.postGrade(p);
            return ResultResponse.<Integer>builder()
                    .resultMessage(result == 1 ? "점수 등록 완료" : "점수 등록 실패")
                    .resultData(result)
                    .build();
    }

    @GetMapping
    @Operation(summary = "수강생 한 명의 시험 점수 가져오기")
    public ResultResponse<List<GradeGetDto>> selGradeScore(@ModelAttribute @ParameterObject GradeGetReq p) {
        List<GradeGetDto> result = service.selGradeScore(p);
        return ResultResponse.<List<GradeGetDto>>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(result)
                .build();
    }

    @GetMapping("gradeDetail")
    @Operation(summary = "성적 상세보기")
    public ResultResponse<List<GradeDetailDto>> selGradeDetail(@ModelAttribute @ParameterObject GradeDetailGetReq p) {
            List<GradeDetailDto> result = service.selGradeDetail(p);
            return ResultResponse.<List<GradeDetailDto>>builder()
                    .resultMessage(userMessage.getMessage())
                    .resultData(result)
                    .build();
    }


    @GetMapping("status")
    @Operation(summary = "시험 처리 상태 가져오기")
    public ResultResponse<List<GradeStatusGetDto>> selGradeStatus(@ModelAttribute @ParameterObject GradeStatusGetReq p) {
        List<GradeStatusGetDto> result = service.selGradeStatus(p);
        return ResultResponse.<List<GradeStatusGetDto>>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(result)
                .build();
    }

    @GetMapping("userAndParent")
    @Operation(summary = "부모,수험생 목록 가져오기")
    public ResultResponse<List<GradeUserAndParentGetRes>> selGradeUserAndParent(@ModelAttribute @ParameterObject GradeUserAndParentGetReq p) {
        List<GradeUserAndParentGetRes> result = service.selGradeUserAndParent(p);
        return ResultResponse.<List<GradeUserAndParentGetRes>>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(result)
                .build();
    }

    @PutMapping
    @Operation(summary = "시험 점수 수정하기")
    public ResultResponse<Integer> updGradeScore(@RequestBody GradePutReq p) {
        Integer result = service.updGradeScore(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(result == 1 ? "점수 수정 완료" : "점수 수정 실패")
                .resultData(result)
                .build();
    }
}
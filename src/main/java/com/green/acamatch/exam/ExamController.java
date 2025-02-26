package com.green.acamatch.exam;

import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.exam.model.ExamPostReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "시험 관리", description = "시험 등록")
@RestController
@RequestMapping("exam")
@RequiredArgsConstructor
public class ExamController {
    private final ExamService service;

    @PostMapping
    @Operation(summary = "시험 등록 하기")
    public ResultResponse<Integer> postSubject(@RequestBody ExamPostReq p) {
            Integer result = service.postExam(p);
            return ResultResponse.<Integer>builder()
                    .resultMessage(result == 1 ? "시험 등록 성공" : "시험 등록 실패")
                    .resultData(result)
                    .build();
    }
}
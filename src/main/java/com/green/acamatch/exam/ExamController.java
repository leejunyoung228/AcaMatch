package com.green.acamatch.exam;

import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.exam.model.ExamPostReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "시험 관리", description = "시험 등록")
@RestController
@RequestMapping("subject")
@RequiredArgsConstructor
public class ExamController {
    private final ExamService service;

    @PostMapping
    @Operation(summary = "시험 등록 하기")
    public ResultResponse<Integer> postSubject(@RequestBody ExamPostReq p) {
        try {
            Integer result = service.postExam(p);
            return ResultResponse.<Integer>builder()
                    .resultMessage("시험 등록에 성공하였습니다.")
                    .resultData(result)
                    .build();
        } catch (IllegalArgumentException e) {
            return ResultResponse.<Integer>builder()
                    .resultMessage(e.getMessage())
                    .resultData(0)
                    .build();
        }
    }
}
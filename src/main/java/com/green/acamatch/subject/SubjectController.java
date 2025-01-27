package com.green.acamatch.subject;

import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.subject.model.SubjectPostReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "시험 회차 관리", description = "시험 회차 등록")
@RestController
@RequestMapping("subject")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService service;
    private final UserMessage userMessage;

    @PostMapping
    @Operation(summary = "시험 회차 등록 하기")
    public ResultResponse<Integer> postSubject(@RequestBody SubjectPostReq p) {
        try {
            Integer result = service.postSubject(p);
            return ResultResponse.<Integer>builder()
                    .resultMessage("시험 회차 등록에 성공하였습니다.")
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
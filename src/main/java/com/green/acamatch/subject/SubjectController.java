package com.green.acamatch.subject;

import com.green.acamatch.acaClass.model.AcaClassDto;
import com.green.acamatch.acaClass.model.AcaClassGetReq;
import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.subject.model.SubjectGetDto;
import com.green.acamatch.subject.model.SubjectGetReq;
import com.green.acamatch.subject.model.SubjectPostReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping()
    @Operation(summary = "시험 처리 상태 가져오기/ null일 경우 resultData 반환 값이 없습니다.")
    public ResultResponse<List<SubjectGetDto>> selSubject(@ModelAttribute @ParameterObject SubjectGetReq p) {
        List<SubjectGetDto> result = service.selSubject(p);
        return ResultResponse.<List<SubjectGetDto>>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(result)
                .build();
    }
}
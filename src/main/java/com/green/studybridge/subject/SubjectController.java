package com.green.studybridge.subject;

import com.green.studybridge.config.model.ResultResponse;
import com.green.studybridge.subject.model.SubjectGetDto;
import com.green.studybridge.subject.model.SubjectGetReq;
import com.green.studybridge.subject.model.SubjectPostReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "시험 회차 관리", description = "시험 회차 등록, 시험 회차 점수 불러오기, 수정, 삭제")
@RestController
@RequestMapping("subject")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService service;

    @PostMapping
    public ResultResponse<Integer> postSubject(@RequestBody SubjectPostReq p) {
        Integer result = service.postSubject(p);
        return ResultResponse.<Integer>builder()
                .resultMessage("시험 회차 등록 완료")
                .resultData(result)
                .build();
    }

    @GetMapping
    @Operation(summary = "시험 회차 점수 가져오기")
    public ResultResponse<List<SubjectGetDto>> selSubjectScore(@ModelAttribute @ParameterObject SubjectGetReq p) {
        List<SubjectGetDto> result = service.selSubjectScore(p);
        return ResultResponse.<List<SubjectGetDto>>builder()
                .resultMessage("시험 회차 점수 출력 완료")
                .resultData(result)
                .build();
    }
}

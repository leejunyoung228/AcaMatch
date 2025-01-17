package com.green.studybridge.subject;

import com.green.studybridge.config.model.ResultResponse;
import com.green.studybridge.subject.model.SubjectPostReq;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "과목 점수 관리", description = "과목 점수 등록, 과목 점수 불러오기, 수정, 삭제")
@RestController
@RequestMapping("subject")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService service;

    @PostMapping
    public ResultResponse<Integer> postSubject(@RequestBody SubjectPostReq p) {
        Integer result = service.postSubject(p);
        return ResultResponse.<Integer>builder()
                .resultMessage("과목 점수 등록 완료")
                .resultData(result)
                .build();
    }
}

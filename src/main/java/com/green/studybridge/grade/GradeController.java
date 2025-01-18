package com.green.studybridge.grade;

import com.green.studybridge.config.model.ResultResponse;
import com.green.studybridge.grade.model.GradePostReq;
import com.green.studybridge.subject.model.SubjectPostReq;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("grade")
public class GradeController {
    private final GradeService service;

    @PostMapping
    public ResultResponse<Integer> postGrade(@RequestBody GradePostReq p) {
        Integer result = service.postGrade(p);
        return ResultResponse.<Integer>builder()
                .resultMessage("성적 등록 완료")
                .resultData(result)
                .build();
    }
}
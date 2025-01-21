package com.green.studybridge.joinClass;

import com.green.studybridge.config.model.ResultResponse;
import com.green.studybridge.joinClass.model.JoinClassDel;
import com.green.studybridge.joinClass.model.JoinClassPostReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 수업 등록, 삭제 관리", description = "user 수업 등록")
@RestController
@RequestMapping("joinClass")
@RequiredArgsConstructor
public class JoinClassController {
    private final JoinClassService service;

    @PostMapping
    @Operation(summary = "user 수업에 등록하기")
    public ResultResponse<Integer> postJoinClass(@RequestBody JoinClassPostReq p) {
        Integer result = service.postJoinClass(p);
        return ResultResponse.<Integer>builder()
                .resultMessage("사용자 수업에 등록 완료")
                .resultData(result)
                .build();
    }

    @DeleteMapping
    @Operation(summary = "등록한 수업에 user 삭제하기")
    public ResultResponse<Integer> deleteJoinClass(@ModelAttribute @ParameterObject JoinClassDel p) {
        Integer result = service.delJoinClass(p);
        return ResultResponse.<Integer>builder()
                .resultMessage("등록한 수업에 사용자 삭제 완료")
                .resultData(result)
                .build();
    }
}
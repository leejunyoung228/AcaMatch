package com.green.acamatch.joinClass;

import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.joinClass.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "수강 관리", description = "수강 신청, 불러오기, 수정, 삭제")
@RestController
@RequestMapping("joinClass")
@RequiredArgsConstructor
public class JoinClassController {
    private final JoinClassService service;
    private final UserMessage userMessage;

    @PostMapping
    @Operation(summary = "수강 신청 하기")
    public ResultResponse<Integer> postJoinClass(@RequestBody JoinClassPostReq p) {
        Integer result = service.postJoinClass(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(result == 1 ? "수강신청 성공" : "수강신청 실패")
                .resultData(result)
                .build();
    }

    @GetMapping
    @Operation(summary = "user가 신청한 학원명/강좌명 / 학생id / 부모id 하나만 적으셔도 됩니다.")
    public ResultResponse<List<JoinClassDto>> selJoinClass(@ModelAttribute @ParameterObject JoinClassGetReq p) {
        List<JoinClassDto> result = service.selJoinClass(p);
        return ResultResponse.<List<JoinClassDto>>builder()
                .resultMessage("불러오기 성공")
                .resultData(result)
                .build();
    }

    @PutMapping
    @Operation(summary = "등록 인가 수정하기")
    public ResultResponse<Integer> putJoinClass(@RequestBody JoinClassPutReq p) {
        Integer result = service.putJoinClass(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(result == 1 ? "수정 완료" : "수정 실패")
                .resultData(result)
                .build();
    }

    @DeleteMapping
    @Operation(summary = "수강 신청 취소하기")
    public ResultResponse<Integer> deleteJoinClass(@ModelAttribute @ParameterObject JoinClassDel p) {
        Integer result = service.delJoinClass(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(result == 1 ? "신청 취소 완료" : "신청 취소 실패")
                .resultData(result)
                .build();
    }
}
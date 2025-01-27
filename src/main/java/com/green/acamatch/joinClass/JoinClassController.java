package com.green.acamatch.joinClass;

import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.grade.model.GradeUserDto;
import com.green.acamatch.grade.model.GradeUserGetReq;
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
    @Operation(summary = "수강 신청 하기 / true = 1 , false = 0")
    public ResultResponse<Integer> postJoinClass(@RequestBody JoinClassPostReq p) {
        try {
        Integer result = service.postJoinClass(p);
        return ResultResponse.<Integer>builder()
                .resultMessage("사용자가 수강 신청하였습니다.")
                .resultData(result)
                .build();
        } catch (IllegalArgumentException e) {
            return ResultResponse.<Integer>builder()
                    .resultMessage(e.getMessage())
                    .resultData(0)
                    .build();
        }
    }

    @GetMapping
    @Operation(summary = "학원명/강좌명 / null일 경우 resultData 반환 값이 없습니다.")
    public ResultResponse<List<JoinClassDto>> selJoinClass(@ModelAttribute @ParameterObject JoinClassGetReq p) {
        List<JoinClassDto> result = service.selJoinClass(p);
        return ResultResponse.<List<JoinClassDto>>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(result)
                .build();
    }

    @PutMapping
    @Operation(summary = "할인, 등록인가 수정하기 / true = 1 , false = 0")
    public ResultResponse<Integer> putJoinClass(@RequestBody JoinClassPutReq p) {
        Integer result = service.putJoinClass(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(result)
                .build();
    }

    @DeleteMapping
    @Operation(summary = "수강 신청 취소하기")
    public ResultResponse<Integer> deleteJoinClass(@ModelAttribute @ParameterObject JoinClassDel p) {
        Integer result = service.delJoinClass(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(result)
                .build();
    }
}
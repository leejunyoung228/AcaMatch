package com.green.acamatch.acaClass;

import com.green.acamatch.acaClass.model.*;
import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "수업 관리", description = "메뉴 등록, 메뉴 불러오기, 수정, 삭제")
@RestController
@RequestMapping("acaClass")
@RequiredArgsConstructor
public class AcaClassController {
    private final AcaClassService service;
    private final UserMessage userMessage;

    @PostMapping
    @Operation(summary = "class 등록하기")
    public ResultResponse<Integer> postAcaClass(@RequestBody AcaClassPostReq p) {
        try {
            Integer result = service.postAcaClass(p);
            return ResultResponse.<Integer>builder()
                    .resultMessage("수업 등록에 성공하였습니다.")
                    .resultData(result)
                    .build();
        } catch (IllegalArgumentException e) {
            return ResultResponse.<Integer>builder()
                    .resultMessage(e.getMessage())
                    .build();
        }
    }

    @PostMapping("weekdays")
    @Operation(summary = "요일 등록하기/순서 맞게 월,화,수,목,금,토,일 등록")
    public ResultResponse<Integer> insWeekDay(@RequestBody AcaClassWeekDay p) {
        try {
            Integer result = service.insWeekDay(p);
            return ResultResponse.<Integer>builder()
                    .resultMessage("요일 등록에 성공하였습니다.")
                    .resultData(result)
                    .build();
        } catch (IllegalArgumentException e) {
            return ResultResponse.<Integer>builder()
                    .resultMessage(e.getMessage())
                    .resultData(0)
                    .build();
        }
    }

    @PostMapping("classweekdays")
    @Operation(summary = "요일 관계 등록하기")
    public ResultResponse<Integer> insAcaClassClassWeekDays(@RequestBody AcaClassWeekDaysRelation p) {
        try {
            Integer result = service.insAcaClassClassWeekDays(p);
            return ResultResponse.<Integer>builder()
                    .resultMessage("요일 관계 등록에 성공하였습니다.")
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
    @Operation(summary = "class 상세 정보 가져오기/ null일 경우 resultData 반환 값이 없습니다.")
    public ResultResponse<List<AcaClassDto>> getClass(@ModelAttribute @ParameterObject AcaClassGetReq p) {
        List<AcaClassDto> result = service.getClass(p);
        return ResultResponse.<List<AcaClassDto>>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(result)
                .build();
    }

    @GetMapping("acaClassToUser")
    @Operation(summary = "특정 user가 등록한 class 가져오기/ null일 경우 resultData 반환 값이 없습니다.")
    public ResultResponse<List<AcaClassToUserDto>> getUserClass(@ModelAttribute @ParameterObject AcaClassToUserGetReq p) {
        List<AcaClassToUserDto> result = service.getUserClass(p);
        return ResultResponse.<List<AcaClassToUserDto>>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(result)
                .build();
    }

    @GetMapping("acaClassUser")
    @Operation(summary = "class에 등록한 user 가져오기/ null일 경우 resultData 반환 값이 없습니다.")
    public ResultResponse<List<AcaClassUserDto>> getClassUser(@ModelAttribute @ParameterObject AcaClassUserGetReq p) {
        List<AcaClassUserDto> result = service.getClassUser(p);
        return ResultResponse.<List<AcaClassUserDto>>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(result)
                .build();
    }

    @PutMapping
    @Operation(summary = "class 수정하기")
    public ResultResponse<Integer> putAcaClass(@RequestBody AcaClassPutReq p) {
        Integer result = service.updAcaClass(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(result)
                .build();
    }

    @DeleteMapping
    @Operation(summary = "class 삭제하기")
    public ResultResponse<Integer> delAcaClass(@ModelAttribute @ParameterObject AcaClassDelReq p) {
        Integer result = service.delAcaClass(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(result)
                .build();
    }

    @DeleteMapping("acaClassDay")
    @Operation(summary = "class 요일 삭제하기/ 강의가 열렸던 날 삭제")
    public ResultResponse<Integer> delAcaClassDay(@ModelAttribute @ParameterObject AcaClassWeekDaysRelation p) {
        Integer result = service.delAcaClassDay(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(result)
                .build();
    }
}
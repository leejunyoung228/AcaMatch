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

@Tag(name = "강좌 관리", description = "강좌 등록, 불러오기, 수정, 삭제")
@RestController
@RequestMapping("acaClass")
@RequiredArgsConstructor
public class AcaClassController {
    private final AcaClassService service;
    private final UserMessage userMessage;

    @PostMapping
    @Operation(summary = "강좌 등록하기")
    public ResultResponse<Integer> postAcaClass(@RequestBody AcaClassPostReq p) {
        Integer result = service.postAcaClass(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(result == 1 ? "강좌 등록 성공" : "강좌 등록 실패")
                .resultData(result)
                .build();
    }

    @PostMapping("weekdays")
    @Operation(summary = "요일 등록하기/순서 맞게 월,화,수,목,금,토,일 등록")
    public ResultResponse<Integer> insWeekDay(@RequestBody WeekDays p) {
        Integer result = service.insWeekDay(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(result == 1 ? "요일 등록 성공" : "요일 등록 실패")
                .resultData(result)
                .build();
    }

    @PostMapping("dayRelation")
    @Operation(summary = "요일 관계 등록하기")
    public ResultResponse<Integer> insAcaClassClassWeekDays(@RequestBody ClassWeekDaysReq p) {
        Integer result = service.insAcaClassClassWeekDays(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(result == 1 ? "요일 관계 등록 성공" : "요일 관계 등록 실패")
                .resultData(result)
                .build();
    }

    @PostMapping("classCategory")
    @Operation(summary = "카테고리 관계 등록하기")
    public ResultResponse<Integer> insAcaClassCategory(@RequestBody AcaClassCategoryReq p) {
        Integer result = service.insAcaClassCategory(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(result == 1 ? "카테고리 등록 성공" : "카테고리 등록 실패")
                .resultData(result)
                .build();
    }

    @GetMapping("detail")
    @Operation(summary = "강좌 상세 정보 가져오기/ 요일과 카테고리 등록이 안된 경우 []로 뜹니다.")
    public ResultResponse<List<AcaClassDetailDto>> getClass(@ModelAttribute @ParameterObject AcaClassDetailGetReq p) {
        List<AcaClassDetailDto> result = service.getClass(p);
        return ResultResponse.<List<AcaClassDetailDto>>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(result)
                .build();
    }

    @GetMapping("acaClassUser")
    @Operation(summary = "수강생 정보 가져오기/ null일 경우 resultData 반환 값이 없습니다.")
    public ResultResponse<List<AcaClassUserDto>> getClassUser(@ModelAttribute @ParameterObject AcaClassUserGetReq p) {
        List<AcaClassUserDto> result = service.getClassUser(p);
        return ResultResponse.<List<AcaClassUserDto>>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(result)
                .build();
    }

    @GetMapping
    @Operation(summary = "학원 강좌 가져오기/ null일 경우 resultData 반환 값이 없습니다.")
    public ResultResponse<List<AcaClassDto>> selAcaClass(@ModelAttribute @ParameterObject AcaClassGetReq p) {
        List<AcaClassDto> result = service.selAcaClass(p);
        return ResultResponse.<List<AcaClassDto>>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(result)
                .build();
    }


    @PutMapping
    @Operation(summary = "강좌 수정하기")
    public ResultResponse<Integer> putAcaClass(@RequestBody AcaClassPutReq p) {
        Integer result = service.updAcaClass(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(result == 1 ? "강좌 수정 성공" : "강좌 수정 실패")
                .resultData(result)
                .build();
    }

    @DeleteMapping
    @Operation(summary = "강좌 삭제하기")
    public ResultResponse<Integer> delAcaClass(@ModelAttribute @ParameterObject AcaClassDelReq p) {
        Integer result = service.delAcaClass(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(result == 1 ? "강좌 삭제 성공" : "강좌 삭제 실패")
                .resultData(result)
                .build();
    }

    @DeleteMapping("day")
    @Operation(summary = "강좌 요일 삭제하기/ 강좌가 열렸던 날 삭제")
    public ResultResponse<Integer> delAcaClassDay(@ModelAttribute @ParameterObject ClassWeekDaysReq p) {
        Integer result = service.delAcaClassDay(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(result == 1 ? "강좌 요일 삭제 성공" : "강좌 요일 삭제 실패")
                .resultData(result)
                .build();
    }
}
package com.green.acamatch.acaClass;

import com.green.acamatch.acaClass.model.*;
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

    @PostMapping
    @Operation(summary = "class 등록하기")
    public ResultResponse<Integer> postAcaClass(@RequestBody AcaClassPostReq p) {
        try {
            Integer result = service.postAcaClass(p);
            return ResultResponse.<Integer>builder()
                    .resultMessage("수업 등록 완료")
                    .resultData(result)
                    .build();
        } catch (IllegalArgumentException e) {
            return ResultResponse.<Integer>builder()
                    .resultMessage(e.getMessage())
                    .build();
        }
    }

    @PostMapping("weekdays")
    @Operation(summary = "요일 등록하기")
    public ResultResponse<Integer> insWeekDay(@RequestBody AcaClassWeekDay p){
        Integer result = service.insWeekDay(p);
        return ResultResponse.<Integer>builder()
                .resultMessage("요일 등록 완료")
                .resultData(result)
                .build();
    }

    @PostMapping("classweekdays")
    @Operation(summary = "요일 관계 등록하기")
    public ResultResponse<Integer> insAcaClassClassWeekDays(@RequestBody AcaClassClassWeekDays p){
        Integer result = service.insAcaClassClassWeekDays(p);
        return ResultResponse.<Integer>builder()
                .resultMessage("요일 관계 등록 완료")
                .resultData(result)
                .build();
    }


    @GetMapping
    @Operation(summary = "class 상세 정보 가져오기")
    public ResultResponse<List<AcaClassDto>> getClass(@ModelAttribute @ParameterObject AcaClassGetReq p) {
        List<AcaClassDto> result = service.getClass(p);
        return ResultResponse.<List<AcaClassDto>>builder()
                             .resultMessage("수업 출력 완료")
                             .resultData(result)
                             .build();
    }

    @GetMapping("acaClassToUser")
    @Operation(summary = "특정 user가 등록한 class 가져오기")
    public ResultResponse<List<AcaClassToUserDto>> getUserClass(@ModelAttribute @ParameterObject AcaClassToUserGetReq p) {
        List<AcaClassToUserDto> result = service.getUserClass(p);
        return ResultResponse.<List<AcaClassToUserDto>>builder()
                             .resultMessage("수업 출력 완료")
                             .resultData(result)
                             .build();
    }

    @GetMapping("acaClassUser")
    @Operation(summary = "class에 등록한 user 가져오기")
    public ResultResponse<List<AcaClassUserDto>> getClassUser(@ModelAttribute @ParameterObject AcaClassUserGetReq p) {
        List<AcaClassUserDto> result = service.getClassUser(p);
        return ResultResponse.<List<AcaClassUserDto>>builder()
                .resultMessage("수업에 등록한 사용자 출력 완료")
                .resultData(result)
                .build();
    }

    @PutMapping
    @Operation(summary = "class 수정하기")
    public ResultResponse<Integer> putAcaClass(@RequestBody AcaClassPutReq p) {
        Integer result = service.updAcaClass(p);
        return ResultResponse.<Integer>builder()
                             .resultMessage("수업 수정 완료")
                             .resultData(result)
                             .build();
    }

    @DeleteMapping
    @Operation(summary = "class 삭제하기")
    public ResultResponse<Integer> delAcaClass(@ModelAttribute @ParameterObject AcaClassDelReq p) {
       Integer result = service.delAcaClass(p);
       return ResultResponse.<Integer>builder()
               .resultMessage("수업 삭제 완료")
               .resultData(result)
               .build();
    }
}
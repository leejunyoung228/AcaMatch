package com.green.studybridge.acaClass;

import com.green.studybridge.acaClass.model.*;
import com.green.studybridge.config.model.ResultResponse;
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
public class acaClassController {
    private final acaClassService service;

    @PostMapping
    @Operation(summary = "class 등록하기")
    public ResultResponse<Integer> postAcaClass(@RequestBody acaClassPostReq p) {
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

    @GetMapping("acaClass")
    @Operation(summary = "class 가져오기")
    public ResultResponse<List<acaClassDto>> getClass(@ModelAttribute @ParameterObject acaClassGetReq p) {
        List<acaClassDto> result = service.getClass(p);
        return ResultResponse.<List<acaClassDto>>builder()
                             .resultMessage("수업 출력 완료")
                             .resultData(result)
                             .build();
    }

    @GetMapping("userAcaClass")
    @Operation(summary = "userClass 가져오기")
    public ResultResponse<List<acaClassUserDto>> getClass(@ModelAttribute @ParameterObject acaClassUserGetReq p) {
        List<acaClassUserDto> result = service.getUserClass(p);
        return ResultResponse.<List<acaClassUserDto>>builder()
                             .resultMessage("수업 출력 완료")
                             .resultData(result)
                             .build();
    }

    @PutMapping
    @Operation(summary = "class 수정하기")
    public ResultResponse<Integer> putAcaClass(@RequestBody acaClassPutReq p) {
        Integer result = service.updAcaClass(p);
        return ResultResponse.<Integer>builder()
                             .resultMessage("수업 수정 완료")
                             .resultData(result)
                             .build();
    }

    @DeleteMapping
    @Operation(summary = "class 삭제하기")
    public ResultResponse<Integer> delAcaClass(@ModelAttribute @ParameterObject acaClassDelReq p) {
       Integer result = service.delAcaClass(p);
       return ResultResponse.<Integer>builder()
               .resultMessage("수업 삭제 완료")
               .resultData(result)
               .build();
    }
}
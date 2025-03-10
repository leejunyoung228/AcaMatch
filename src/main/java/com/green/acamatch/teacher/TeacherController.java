
package com.green.acamatch.teacher;

import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.teacher.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "선생님 관리", description = "선생님 등록, 수정, 삭제")
@RestController
@RequestMapping("teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @Operation(summary = "선생님 등록하기 / isActive는 선생님 퇴사 여부 입니다.")
    @PostMapping
    public ResultResponse<Integer> postTeacher(@RequestBody TeacherPostReq p) {
        Integer result = teacherService.postTeacher(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(result == 1 ? "선생님 등록 성공" : "선생님 등록 실패")
                .resultData(result)
                .build();
    }

    @Operation(summary = "선생님 정보 불러오기")
    @GetMapping
    public ResultResponse<List<TeacherInfoGetRes>>getTeacherInfo(@ModelAttribute @ParameterObject TeacherInfoGetReq p) {
        List<TeacherInfoGetRes> result = teacherService.getTeacherInfo(p);
        try {
            return ResultResponse.<List<TeacherInfoGetRes>>builder()
                    .resultMessage("선생님 정보 불러오기 성공")
                    .resultData(result)
                    .build();
        }catch (CustomException e) {
           return ResultResponse.<List<TeacherInfoGetRes>>builder()
                    .resultMessage(e.getMessage())
                    .resultData(new ArrayList<>())
                    .build();
        }
    }

    @Operation(summary = "선생님 승인 정보 불러오기")
    @GetMapping("Agree")
    public ResultResponse<List<TeacherListGetRes>>getTeacherList(@ModelAttribute @ParameterObject TeacherListGetReq p) {
        List<TeacherListGetRes> result = teacherService.getTeacherList(p);
        try {
            return ResultResponse.<List<TeacherListGetRes>>builder()
                    .resultMessage("수업 신청한 선생님 불러오기 성공")
                    .resultData(result)
                    .build();
        }catch (CustomException e) {
            return ResultResponse.<List<TeacherListGetRes>>builder()
                    .resultMessage("수업 신청한 선생님 불러오기 실패")
                    .resultData(new ArrayList<>())
                    .build();
        }
    }

    @Operation(summary = "선생님 정보 수정하기")
    @PutMapping
    public ResultResponse<Integer> updateTeacher(@ModelAttribute @ParameterObject TeacherPutReq p) {
        Integer result = teacherService.updateTeacher(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(result == 1 ? "선생님 정보 수정 성공" : "선생님 정보 수정 실패")
                .resultData(result)
                .build();
    }
    @Operation(summary = "선생님 삭제하기")
    @DeleteMapping
    public ResultResponse<Integer> deleteTeacher(@ModelAttribute @ParameterObject TeacherDelReq p) {
        Integer result = teacherService.deleteTeacher(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(result == 1 ? "선생님 삭제 성공" : "선생님 삭제 실패")
                .resultData(result)
                .build();
    }
}

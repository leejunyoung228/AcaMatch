package com.green.acamatch.teacher;

import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.teacher.model.TeacherDelReq;
import com.green.acamatch.teacher.model.TeacherPostReq;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@Tag(name = "선생님 관리", description = "선생님 등록, 가져오기, 수정, 삭제")
@RestController
@RequestMapping("teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @PostMapping
    public ResultResponse<Integer> postTeacher(@RequestBody TeacherPostReq p) {
        Integer result = teacherService.postTeacher(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(result == 1 ? "선생님 등록 성공" : "선생님 등록 실패")
                .resultData(result)
                .build();
    }

    @DeleteMapping
    public ResultResponse<Integer> deleteTeacher(@ModelAttribute @ParameterObject TeacherDelReq p) {
        Integer result = teacherService.deleteTeacher(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(result == 1 ? "선생님 삭제 성공" : "선생님 삭제 실패")
                .resultData(result)
                .build();
    }
}
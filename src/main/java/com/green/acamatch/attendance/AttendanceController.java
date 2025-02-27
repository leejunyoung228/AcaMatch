package com.green.acamatch.attendance;

import com.green.acamatch.attendance.model.*;
import com.green.acamatch.config.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "출석부 관리", description = "출석부 등록, 가져오기, 수정, 삭제")
@RestController
@RequestMapping("attendance")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PostMapping
    @Operation(summary = "출석부 등록", description = "출석, 지각, 결석, 조퇴")
    public ResultResponse<Integer> postAttendance(@RequestBody AttendancePostReq p) {
        Integer result = attendanceService.postAttendance(p);
        return ResultResponse.<Integer>builder()
                .resultMessage("출석부 등록 성공")
                .resultData(result)
                .build();
    }

    @GetMapping
    @Operation(summary = "출석부 가져오기")
    public ResultResponse<List<AttendanceGetDto>> getAttendanceCount(@ParameterObject AttendanceGetReq p) {
        List<AttendanceGetDto> result = attendanceService.getAttendance(p);
        return ResultResponse.<List<AttendanceGetDto>>builder()
                .resultMessage("출석부 가져오기 성공")
                .resultData(result)
                .build();
    }

    @PutMapping
    @Operation(summary = "출석부 수정" , description = "출석, 지각, 결석, 조퇴")
    public ResultResponse<Integer> putAttendance(@RequestBody AttendancePutReq p) {
        Integer result = attendanceService.updAttendance(p);
        return ResultResponse.<Integer>builder()
                .resultMessage("출석부 수정 성공")
                .resultData(result)
                .build();
    }

    @DeleteMapping
    @Operation(summary = "출석부 삭제")
    public ResultResponse<Integer> deleteAttendance(@ModelAttribute @ParameterObject AttendanceDelReq p) {
        Integer result = attendanceService.delAttendance(p);
        return ResultResponse.<Integer>builder()
                .resultMessage("출석부 삭제 성공")
                .resultData(result)
                .build();
    }
}
package com.green.acamatch.attendance;

import com.green.acamatch.attendance.model.*;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "출석부 관리", description = "출석부 등록, 가져오기, 수정, 삭제")
@RestController
@RequestMapping("attendance")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PostMapping
    @Operation(summary = "출석부 등록", description = "출석, 지각, 결석, 조퇴 - 입력시 한글로 적어야합니다.")
    public ResultResponse<Integer> postAttendance(@RequestBody AttendancePostReq p) {
        Integer result = attendanceService.postAttendance(p);
        return ResultResponse.<Integer>builder()
                .resultMessage("출석부 등록 성공")
                .resultData(result)
                .build();
    }

    @GetMapping
    @Operation(summary = "출석부 가져오기 / 강사입장 출석률")
    public ResultResponse<List<AttendanceGetDto>> getAttendanceCount(@ParameterObject AttendanceGetReq p) {
        try {
            List<AttendanceGetDto> result = attendanceService.getAttendance(p);
            return ResultResponse.<List<AttendanceGetDto>>builder()
                    .resultMessage("출석부 가져오기 성공")
                    .resultData(result)
                    .build();
        } catch (IllegalArgumentException e) {
            return ResultResponse.<List<AttendanceGetDto>>builder()
                    .resultMessage(e.getMessage())
                    .resultData(new ArrayList<>())
                    .build();
        }
    }

    @GetMapping("academyCount")
    @Operation(summary = "학원입장 출석률")
    public ResultResponse<List<AcademyAttendanceGetRes>> getAcademyAttendanceStatusCount(@ParameterObject AcademyAttendanceGetReq p) {
        try {
            List<AcademyAttendanceGetRes> result = attendanceService.getAcademyAttendanceStatusCount(p);
            return ResultResponse.<List<AcademyAttendanceGetRes>>builder()
                    .resultMessage("학원입장 출석률 가져오기 성공")
                    .resultData(result)
                    .build();
        } catch (CustomException e) {
            return ResultResponse.<List<AcademyAttendanceGetRes>>builder()
                    .resultMessage("학원입장 출석률 가져오기 실패")
                    .resultData(new ArrayList<>())
                    .build();
        }
    }

    @GetMapping("user")
    @Operation(summary = "수강생 목록 가져오기")
    public ResultResponse<List<AttendanceGetUserDto>> getAttendanceUser(@ParameterObject AttendanceGetUserReq p) {
        try {
            List<AttendanceGetUserDto> result = attendanceService.getAttendanceUser(p);
            return ResultResponse.<List<AttendanceGetUserDto>>builder()
                    .resultMessage("출석부 가져오기 성공")
                    .resultData(result)
                    .build();
        } catch (IllegalArgumentException e) {
            return ResultResponse.<List<AttendanceGetUserDto>>builder()
                    .resultMessage(e.getMessage())
                    .resultData(new ArrayList<>())
                    .build();
        }
    }

    @PutMapping
    @Operation(summary = "출석부 수정", description = "출석, 지각, 결석, 조퇴")
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
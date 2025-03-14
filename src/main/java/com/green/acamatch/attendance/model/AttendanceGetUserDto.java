package com.green.acamatch.attendance.model;

import com.green.acamatch.entity.attendance.AttendanceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AttendanceGetUserDto {
    @Schema(title = "출석부 pk", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long attendanceId;
    @Schema(title = "유저 pk", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long userId;
    @Schema(title = "이름", example = "홍길동", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(title = "출석 상태", requiredMode = Schema.RequiredMode.REQUIRED)
    private AttendanceStatus status;
}
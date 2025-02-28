package com.green.acamatch.attendance.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class AttendanceGetUserReq {
    @Schema(title = "학원 pk", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long acaId;
    @Schema(title = "강좌 pk", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long classId;
    @Schema(title = "출석부 조회 날짜", example = "2025-02-28", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate attendanceDate;
}
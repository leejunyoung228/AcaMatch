package com.green.acamatch.attendance.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class AttendanceGetReq {
    @Schema(title = "학원 pk", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long acaId;
    @Schema(title = "강좌 pk", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long classId;
    @Schema(title = "조회 시작 날짜", example = "2025-02-01")
    private LocalDate startDate;
    @Schema(title = "조회 종료 날짜", example = "2025-02-28")
    private LocalDate endDate;
}
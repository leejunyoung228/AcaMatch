package com.green.acamatch.attendance.model;

import com.green.acamatch.entity.attendance.AttendanceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class AttendancePutReq {
    @Schema(title = "출석부 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long attendanceId;
    @Schema(title = "조인 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long joinClassId;
    @Schema(title = "출석 날짜",example = "2025-02-27", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate attendanceDate;
    @Schema(title = "출석 상태")
    private AttendanceStatus status;
}

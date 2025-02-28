package com.green.acamatch.attendance.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AttendanceDelReq {
    @Schema(title = "출석부 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long attendanceId;
}
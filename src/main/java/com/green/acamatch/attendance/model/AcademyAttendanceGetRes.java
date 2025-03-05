package com.green.acamatch.attendance.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class AcademyAttendanceGetRes {
    @Schema(title = "학원 pk", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long acaId;
    @Schema(title = "학원 이름", example = "그린 컴퓨터 아트 학원", requiredMode = Schema.RequiredMode.REQUIRED)
    private String acaName;
    @Schema(title = "출석한 사람 수")
    private int present;
    @Schema(title = "지각한 사람 수")
    private int late;
    @Schema(title = "결석한 사람 수")
    private int absent;
    @Schema(title = "조퇴한 사람 수")
    private int earlyLeave;
    @Schema(title = "총 합")
    private int sumCount;
}
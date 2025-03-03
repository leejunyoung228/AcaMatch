package com.green.acamatch.attendance.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class AttendanceGetDto {
    @Schema(title = "학원 pk", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long acaId;
    @Schema(title = "강좌 pk", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long classId;
    @Schema(title = "학원 이름", example = "그린 컴퓨터 아트 학원", requiredMode = Schema.RequiredMode.REQUIRED)
    private String acaName;
    @Schema(title = "강좌 이름", example = "프론트 수업", requiredMode = Schema.RequiredMode.REQUIRED)
    private String className;
    @Schema(title = "출석한 사람 수")
    private int present;
    @Schema(title = "지각한 사람 수")
    private int late;
    @Schema(title = "결석한 사람 수")
    private int absent;
    @Schema(title = "조퇴한 사람 수")
    private int earlyLeave;
    @Schema(title = "출석 날짜" , example = "2025-02-28")
    private LocalDate attendanceDate;
    @Schema(title = "총 합")
    private int sumCount;
}

package com.green.acamatch.acaClass.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
public class AcaClassPutReq {

    @Schema(title = "학원 PK", example = "324", requiredMode = Schema.RequiredMode.REQUIRED)
    private long acaId;

    @Schema(title = "강좌 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long classId;

    @Schema(title = "강좌 이름", example = "원어민 영어")
    private String className;
    @Schema(title = "강좌 설명", example = "원어민급 영어반이다.")
    private String classComment;
    @Schema(title = "수업 시작 날짜", example = "2025-01-23")
    private LocalDate startDate;
    @Schema(title = "수업 종료 날짜", example = "2025-01-31")
    private LocalDate endDate;
    @Schema(title = "수업 시작 시간", example = "09:00")
    private LocalTime startTime;
    @Schema(title = "수업 종료 시간", example = "18:00")
    private LocalTime endTime;
    @Schema(title = "수강료", example = "100,000")
    private int price;
}

package com.green.acamatch.acaClass.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AcaClassPutReq {
    @Schema(title = "학원 PK", example = "324", requiredMode = Schema.RequiredMode.REQUIRED)
    private long acaId;

    @Schema(title = "수업 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long classId;

    @Schema(title = "수업 이름", example = "원어민 영어")
    private String className;
    @Schema(title = "수업 설명", example = "원어민급 영어반이다.")
    private String classComment;
    @Schema(title = "수업 시작 날짜", example = "2025-01-23")
    private String startDate;
    @Schema(title = "수업 종료 날짜", example = "2025-01-31")
    private String endDate;
    @Schema(title = "수업 시작 시간", example = "09:00")
    private String startTime;
    @Schema(title = "수업 종료 시간", example = "18:00")
    private String endTime;
    @Schema(title = "수강료", example = "100,000")
    private int price;
}

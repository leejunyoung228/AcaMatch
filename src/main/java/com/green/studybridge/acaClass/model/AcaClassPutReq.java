package com.green.studybridge.acaClass.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AcaClassPutReq {

    @Schema(title = "수업 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long classId;

    @Schema(title = "학원 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long acaId;

    @Schema(title = "수업 이름", example = "국어")
    private String className;
    @Schema(title = "수업 설명", example = "한국어와 문학에 대한 기본적인 이해를 배우는 과목입니다.")
    private String classComment;
    @Schema(title = "수업 시작 날짜", example = "YYYY-MM-DD")
    private String startDate;
    @Schema(title = "수업 종료 날짜", example = "YYYY-MM-DD")
    private String endDate;
    @Schema(title = "수업 시작 시간", example = "HH:mm")
    private String startTime;
    @Schema(title = "수업 종료 시간", example = "HH:mm")
    private String endTime;
    @Schema(title = "수강료", example = "100,000")
    private int price;
}

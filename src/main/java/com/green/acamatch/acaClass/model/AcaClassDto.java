package com.green.acamatch.acaClass.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AcaClassDto {
    @Schema(title = "학원 PK", example = "1")
    private Long acaId;
    @Schema(title = "학원 사진 여러장")
    private String acaPics;
    @Schema(title = "학원 사진")
    private String acaPic;
    @Schema(title = "학원 이름")
    private String acaName;
    @Schema(title = "강좌 PK", example = "1")
    private Long classId;
    @Schema(title = "강좌 이름", example = "초등 국어")
    private String className;
    @Schema(title = "수업 시작 날짜", example = "2025-01-16")
    private String startDate;
    @Schema(title = "수업 종료 날짜", example = "2025-01-30")
    private String endDate;
    @Schema(title = "선생님 ID")
    private long teacherId;
    @Schema(title = "학원관계자 ID")
    private long academyId;
    @Schema(title = "선생님 이름")
    private String teacherName;
}

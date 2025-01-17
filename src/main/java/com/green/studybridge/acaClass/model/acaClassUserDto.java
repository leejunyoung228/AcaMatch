package com.green.studybridge.acaClass.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class acaClassUserDto {
    @JsonIgnore
    private long userId;

    @JsonIgnore
    private long classId;

    @Schema(title = "수업 이름", example = "국어")
    private String className;
    @Schema(title = "수업 시작 날짜", example = "2025-01-16")
    private String startDate;
    @Schema(title = "수업 종료 날짜", example = "2025-01-30")
    private String endDate;
    @Schema(title = "학원 사진")
    private String acaPic;
}

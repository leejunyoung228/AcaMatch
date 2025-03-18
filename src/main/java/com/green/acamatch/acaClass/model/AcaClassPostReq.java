package com.green.acamatch.acaClass.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.acamatch.entity.manager.TeacherIds;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
public class AcaClassPostReq {
    @JsonIgnore
    private long classId;

    @Schema(title = "학원 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long acaId;
    @Schema(title = "강좌 이름", example = "국어")
    private String className;
    @Schema(title = "강좌 설명", example = "한국어와 문학에 대한 기본적인 이해를 배우는 과목입니다.")
    private String classComment;
    @Schema(title = "강좌 시작 날짜", example = "2025-01-16")
    private LocalDate startDate;
    @Schema(title = "강좌 종료 날짜", example = "2025-01-30")
    private LocalDate endDate;
    @Schema(title = "강좌 시작 시간", example = "09:00")
    private LocalTime startTime;
    @Schema(title = "강좌 종료 시간", example = "18:00")
    private LocalTime endTime;
    @Schema(title = "수강료", example = "100000")
    private int price;
}
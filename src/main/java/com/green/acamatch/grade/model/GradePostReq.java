package com.green.acamatch.grade.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GradePostReq {

    @Schema(title = "수강생 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long joinClassId;
    @Schema(title = "과목 점수 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long subjectId;
    @Schema(title = "과목 점수", example = "90")
    private int score;
    @Schema(title = "패스 여부", example = "0")
    private Integer pass;
    @Schema(title = "시험 날짜", example = "2025-01-23")
    private String examDate;
    @Schema(title = "처리 상태", example = "0")
    private int processingStatus;
}
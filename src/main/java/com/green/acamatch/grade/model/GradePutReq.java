package com.green.acamatch.grade.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class GradePutReq {
    @JsonIgnore
    private Long classId;

    @Schema(title = "성적 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long gradeId;
    @Schema(title = "시험 날짜", example = "2025-02-10")
    private LocalDate examDate;
    @Schema(title = "성적", example = "10")
    private Integer score;
    @Schema(title = "통과 여부", example = "1")
    private Integer pass;
    @Schema(title = "처리 상태", example = "1")
    private int processingStatus;
}

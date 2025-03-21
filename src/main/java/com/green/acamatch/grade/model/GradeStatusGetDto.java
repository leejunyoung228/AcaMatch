package com.green.acamatch.grade.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class GradeStatusGetDto {
    @Schema(title = "학원 사진 여러장")
    private String acaPics;
    @Schema(title = "학원 사진")
    private String acaPic;
    @Schema(title = "시험 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long examId;
    @Schema(title = "시험 이름", example = "1차 시험")
    private String examName;
    @Schema(title = "시험 날짜", example = "2025-01-25")
    private String examDate;
    @Schema(title = "처리 상태", example = "0")
    private int processingStatus;
    @Schema(title = "학원관계자 ID", example = "1")
    private long academyId;
}

package com.green.acamatch.subject.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class SubjectGetDto {
    @Schema(title = "학원 사진")
    private String acaPic;
    @Schema(title = "시험 이름", example = "1차 시험")
    private String subjectName;
    @Schema(title = "시험 날짜", example = "2025-01-25")
    private String examDate;
    @Schema(title = "처리 상태", example = "0")
    private int processingStatus;
}

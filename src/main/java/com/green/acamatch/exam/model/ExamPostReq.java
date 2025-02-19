package com.green.acamatch.exam.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
public class ExamPostReq {

    @JsonIgnore
    private long examId;

    @Schema(title = "강좌 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long classId;

    @Schema(title = "시험 이름", example = "1차 시험")
    private String examName;

    @Schema(title = "시험 유형", example = "1")
    private int examType;
}
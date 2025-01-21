package com.green.acamatch.subject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SubjectPostReq {

    @JsonIgnore
    private long subjectId;

    @Schema(title = "수업 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long classId;
    @Schema(title = "과목 이름", example = "1차 시험")
    private String subjectName;
    @Schema(title = "과목 유형", example = "1")
    private int scoreType;
}
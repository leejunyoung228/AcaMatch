package com.green.studybridge.subject.model;

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
    @Schema(title = "과목 이름", example = "기하와 벡터")
    private String subjectName;
    @Schema(title = "과목 유형", example = "1")
    private int scoreType;
}
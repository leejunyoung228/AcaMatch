package com.green.studybridge.grade.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GradePutReq {
    @Schema(title = "성적 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long gradeId;

    @Schema(title = "성적", example = "90")
    private int score;
}

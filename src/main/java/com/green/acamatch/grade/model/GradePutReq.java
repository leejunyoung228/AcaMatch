package com.green.acamatch.grade.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GradePutReq {

    @JsonIgnore
    private Long classId;

    @Schema(title = "성적 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long gradeId;

    @Schema(title = "성적", example = "10")
    private int score;
}

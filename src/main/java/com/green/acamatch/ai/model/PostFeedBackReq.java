package com.green.acamatch.ai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostFeedBackReq {
    @Schema(title = "grade PK", example = "1")
    private Integer gradeId;

    @Schema(title = "AI 피드백", example = "피드백")
    private String feedBack;

    @JsonIgnore
    private String message;
}

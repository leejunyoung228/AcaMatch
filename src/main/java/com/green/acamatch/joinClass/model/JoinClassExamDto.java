package com.green.acamatch.joinClass.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class JoinClassExamDto {
    @Schema(title = "시험 날짜", example = "2025-01-24")
    private String examDate;
    @Schema(title = "성적", example = "90")
    private Integer score;
    @Schema(title = "통과 여부", example = "null")
    private String pass;
}

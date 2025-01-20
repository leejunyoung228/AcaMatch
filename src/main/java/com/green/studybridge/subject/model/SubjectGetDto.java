package com.green.studybridge.subject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SubjectGetDto {
    @JsonIgnore
    private long subjectId;
    @Schema(title = "과목 이름", example = "1차 시험")
    private String subjectName;
    @Schema(title = "시험 날짜", example = "YYYY-MM-DD")
    private String examDate;
    @Schema(title = "과목 점수", example = "90")
    private String score;
    @Schema(title = "패스 여부")
    private Integer pass;
}

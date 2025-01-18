package com.green.studybridge.grade.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GradePostReq {
    @JsonIgnore
    private long gradeId;

    private long subjectId;

    private int score;
    private int pass;
}

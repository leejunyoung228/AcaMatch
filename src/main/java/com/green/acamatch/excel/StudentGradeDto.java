package com.green.acamatch.excel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StudentGradeDto {
    private long userId;
    private long gradeId;
    private String name;
    private String subjectName;
    private String examDate;
}

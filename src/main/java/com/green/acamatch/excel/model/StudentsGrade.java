package com.green.acamatch.excel.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StudentsGrade {
    private Long joinClassId;
    private Long userId;
    private Long gradeId;
    private Long subjectId;
    private String Name;
    private String SubjectName;
    private String examDate;
    private Integer score;
    private Integer pass;
    private int processingStatus;
}

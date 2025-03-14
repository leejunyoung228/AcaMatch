package com.green.acamatch.excel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentsGradeDto {
    private Long joinClassId;
    private String name;
    private Long examId;
    private String examName;
    private LocalDate examDate;
    private Long gradeId;
    private Integer examType;
    private Integer score;
    private Integer pass;
    private Integer processingStatus;
}
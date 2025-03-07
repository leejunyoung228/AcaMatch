package com.green.acamatch.excel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudentsGradeDto {
    private Long classId;
    private Long userId;
    private String name;
    private Long examId;
    private String examName;
    private LocalDate examDate;
    private Long gradeId;
    private Integer examType;
    private Integer score;
    private Integer pass;
    private Integer processingStatus;

    public StudentsGradeDto(long classId, long userId, String name,
                            long examId, String examName, LocalDate examDate,
                            long gradeId, Integer score, Integer pass, Integer processingStatus) {
    }
}
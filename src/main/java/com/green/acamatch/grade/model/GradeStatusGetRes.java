package com.green.acamatch.grade.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GradeStatusGetRes {
    private List<GradeStatusGetDto> gradeStatusGetDtoList;
}

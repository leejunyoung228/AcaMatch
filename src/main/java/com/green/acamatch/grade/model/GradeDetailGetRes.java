package com.green.acamatch.grade.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GradeDetailGetRes {
    List<GradeDetailDto> joinClassGradeDtoList;
}

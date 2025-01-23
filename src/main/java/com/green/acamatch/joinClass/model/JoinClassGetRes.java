package com.green.acamatch.joinClass.model;

import com.green.acamatch.grade.model.GradeGetDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class JoinClassGetRes {
    List<JoinClassDto> joinClassList;
}

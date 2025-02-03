package com.green.acamatch.joinClass.model;

import com.green.acamatch.grade.model.GradeGetDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class JoinClassGetRes {
    List<JoinClassDto> joinClassList;
}

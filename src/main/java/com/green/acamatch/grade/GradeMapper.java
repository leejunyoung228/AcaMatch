package com.green.acamatch.grade;

import com.green.acamatch.grade.model.GradePostReq;
import com.green.acamatch.grade.model.GradeGetDto;
import com.green.acamatch.grade.model.GradeGetReq;
import com.green.acamatch.grade.model.GradePutReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GradeMapper {
    int insGrade(GradePostReq p);
    List<GradeGetDto> selGradeScore(GradeGetReq p);
    int updGradeScore(GradePutReq p);
}

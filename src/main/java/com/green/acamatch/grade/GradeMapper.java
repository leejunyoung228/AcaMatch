package com.green.acamatch.grade;

import com.green.acamatch.grade.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GradeMapper {
    int insGrade(GradePostReq p);
    int existsGrade(long joinClassId, long subjectId);
    List<GradeGetDto> selGradeScore(GradeGetReq p);
    List<GradeUserDto> selGradeUser(GradeUserGetReq p);
    List<GradeStatusGetDto> selGradeStatus(GradeStatusGetReq p);
    int updGradeScore(GradePutReq p);
}
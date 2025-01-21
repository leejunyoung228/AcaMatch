package com.green.studybridge.grade;

import com.green.studybridge.grade.model.GradePostReq;
import com.green.studybridge.grade.model.GradeGetDto;
import com.green.studybridge.grade.model.GradeGetReq;
import com.green.studybridge.grade.model.GradePutReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GradeMapper {
    int insGrade(GradePostReq p);
    List<GradeGetDto> selGradeScore(GradeGetReq p);
    int updGradeScore(GradePutReq p);
}

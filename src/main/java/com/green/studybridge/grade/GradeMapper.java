package com.green.studybridge.grade;

import com.green.studybridge.grade.model.GradePostReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GradeMapper {
    int insGrade(GradePostReq p);
}

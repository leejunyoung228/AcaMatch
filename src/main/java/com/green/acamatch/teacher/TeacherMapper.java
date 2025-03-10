package com.green.acamatch.teacher;

import com.green.acamatch.teacher.model.TeacherInfoGetReq;
import com.green.acamatch.teacher.model.TeacherInfoGetRes;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeacherMapper {
    TeacherInfoGetRes getTeacherInfo(TeacherInfoGetReq p);
}
package com.green.acamatch.teacher;

import com.green.acamatch.teacher.model.TeacherListGetReq;
import com.green.acamatch.teacher.model.TeacherListGetRes;
import com.green.acamatch.teacher.model.TeacherInfoGetReq;
import com.green.acamatch.teacher.model.TeacherInfoGetRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeacherMapper {
    List<TeacherInfoGetRes> getTeacherInfo(TeacherInfoGetReq p);
    List<TeacherListGetRes> getTeacherList(TeacherListGetReq p);
}
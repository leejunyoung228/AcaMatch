package com.green.acamatch.joinClass;

import com.green.acamatch.grade.model.GradeUserDto;
import com.green.acamatch.grade.model.GradeUserGetReq;
import com.green.acamatch.joinClass.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JoinClassMapper {
    int insJoinClass(JoinClassPostReq p);
    int existsJoinClass(long classId, long userId);
    List<JoinClassDto> selJoinClass(JoinClassGetReq p);
    int updJoinClass(JoinClassPutReq p);
    int delJoinClass(JoinClassDel p);
}
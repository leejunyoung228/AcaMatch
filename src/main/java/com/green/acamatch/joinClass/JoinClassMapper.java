package com.green.acamatch.joinClass;

import com.green.acamatch.joinClass.model.JoinClassDel;
import com.green.acamatch.joinClass.model.JoinClassPostReq;
import com.green.acamatch.joinClass.model.JoinClassPutReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JoinClassMapper {
    int insJoinClass(JoinClassPostReq p);
    int existsJoinClass(long classId, long userId);
    int updJoinClass(JoinClassPutReq p);
    int delJoinClass(JoinClassDel p);
}

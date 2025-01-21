package com.green.acamatch.joinClass;

import com.green.acamatch.joinClass.model.JoinClassDel;
import com.green.acamatch.joinClass.model.JoinClassPostReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JoinClassMapper {
    int insJoinClass(JoinClassPostReq p);
    int delJoinClass(JoinClassDel p);
}

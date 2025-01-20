package com.green.studybridge.joinClass;

import com.green.studybridge.joinClass.model.JoinClassDel;
import com.green.studybridge.joinClass.model.JoinClassPostReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JoinClassMapper {
    int insJoinClass(JoinClassPostReq p);
    int delJoinClass(JoinClassDel p);
}

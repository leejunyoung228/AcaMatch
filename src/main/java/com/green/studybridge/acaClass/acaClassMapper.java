package com.green.studybridge.acaClass;

import com.green.studybridge.acaClass.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface acaClassMapper {
    int insAcaClass(acaClassPostReq p);
    int existsClass(long acaId, String className);
    List<acaClassDto> selAcaClass(acaClassGetReq p);
    List<acaClassUserDto> selAcaClassUser(acaClassUserGetReq p);
    int updAcaClass(acaClassPutReq p);
    int delAcaClass(acaClassDelReq p);
}
package com.green.studybridge.acaClass;

import com.green.studybridge.acaClass.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AcaClassMapper {
    int insAcaClass(AcaClassPostReq p);
    int existsClass(long acaId, String className);
    int insWeek(AcaClassDay p);
    List<AcaClassDto> selAcaClass(AcaClassGetReq p);
    List<AcaClassToUserDto> selAcaClassToUser(AcaClassToUserGetReq p);
    List<AcaClassUserDto> selAcaClassUser(AcaClassUserGetReq p);
    int updAcaClass(AcaClassPutReq p);
    int delAcaClass(AcaClassDelReq p);
}
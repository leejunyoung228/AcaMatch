package com.green.acamatch.acaClass;

import com.green.acamatch.acaClass.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AcaClassMapper {
    int insAcaClass(AcaClassPostReq p);
    int existsClass(long acaId, String className);
    int insWeekDay(AcaClassWeekDay p);
    int existsDay(String day);
    int insAcaClassClassWeekDays(AcaClassClassWeekDays p);
    int existsClassWeekDays(long dayId, long classId);
    List<AcaClassDto> selAcaClass(AcaClassGetReq p);
    List<AcaClassToUserDto> selAcaClassToUser(AcaClassToUserGetReq p);
    List<AcaClassUserDto> selAcaClassUser(AcaClassUserGetReq p);
    int updAcaClass(AcaClassPutReq p);
    int delAcaClass(AcaClassDelReq p);
    int delAcaClassDay(AcaClassClassWeekDays p);
}
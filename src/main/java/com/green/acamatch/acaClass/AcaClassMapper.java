package com.green.acamatch.acaClass;

import com.green.acamatch.acaClass.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AcaClassMapper {
    int insAcaClass(AcaClassPostReq p);
    int existsClass(long acaId, String className);
    int insWeekDay(WeekDays p);
    int existsDay(String day);
    int insAcaClassClassWeekDays(ClassWeekDays p);
    int existsClassWeekDays(long dayId, long classId);
    int insAcaClassCategory(AcaClassCategoryReq p);
    int existsCategory(long classId, long categoryId);
    List<AcaClassDetailDto> selAcaClassDetail(AcaClassDetailGetReq p);
    List<AcaClassUserDto> selAcaClassUser(AcaClassUserGetReq p);
    List<AcaClassDto> selAcaClass(AcaClassGetReq p);
    int updAcaClass(AcaClassPutReq p);
    int delAcaClass(AcaClassDelReq p);
    int delAcaClassDay(ClassWeekDays p);
}
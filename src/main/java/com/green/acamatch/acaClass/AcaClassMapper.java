package com.green.acamatch.acaClass;

import com.green.acamatch.acaClass.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AcaClassMapper {
    List<AcaClassDetailDto> selAcaClassDetail(AcaClassDetailGetReq p);
    List<AcaClassUserDto> selAcaClassUser(AcaClassUserGetReq p);
    List<AcaClassDto> selAcaClass(AcaClassGetReq p);
}
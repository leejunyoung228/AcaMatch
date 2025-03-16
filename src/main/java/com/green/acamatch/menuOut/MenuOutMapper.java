package com.green.acamatch.menuOut;

import com.green.acamatch.menuOut.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuOutMapper {
    List<MenuOutAcademyAllGetRes> getAcademyAll();
    List<MenuOutAcaClassGetRes> getAcaClass(Long acaId);
    List<MenuOutAcaClassBookGetRes> getAcaClassBook(Long acaId, Long classId);
    List<MenuOutExamGetRes> getExamList(MenuOutExamGetReq p);
    List<MenuOutExamUserGetRes> getExamUser(MenuOutExamUserGetReq p);
}

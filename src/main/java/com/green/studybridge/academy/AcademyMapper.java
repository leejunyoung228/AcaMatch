package com.green.studybridge.academy;

import com.green.studybridge.academy.model.*;
import com.green.studybridge.academy.model.category.CategoryGetAgeRangeRes;
import com.green.studybridge.academy.model.category.CategoryGetDaysRes;
import com.green.studybridge.academy.model.category.CategoryGetLevelRes;
import com.green.studybridge.academy.model.tag.InsTagWithAcademy;
import com.green.studybridge.academy.model.tag.SelTagDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AcademyMapper {
    List<SelTagDto> selTagDtoList();

    int insAcaTag(AcademyPostReq req);

    int insAcademy(AcademyPostReq req);
    int insTagWithAcademy(InsTagWithAcademy p);

    int updAcademy(AcademyUpdateReq req);
    int delAcademy(AcademyDeleteReq req);


// ---------------------------------------------------------------
    List<getAcademyRes> getAcademy(getAcademyReq p);

    GetAcademyDetail getAcademyDetail(Long acaId);
}

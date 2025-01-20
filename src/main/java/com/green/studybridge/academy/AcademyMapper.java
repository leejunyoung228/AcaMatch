package com.green.studybridge.academy;

import com.green.acamatch_mytest.academy.model.*;
import com.green.acamatch_mytest.academy.model.category.CategoryGetAgeRangeRes;
import com.green.acamatch_mytest.academy.model.category.CategoryGetDaysRes;
import com.green.acamatch_mytest.academy.model.category.CategoryGetLevelRes;
import com.green.acamatch_mytest.academy.model.tag.InsTagWithAcademy;
import com.green.acamatch_mytest.academy.model.tag.SelTagDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AcademyMapper {
    List<SelTagDto> selTagDtoList();

    List<CategoryGetAgeRangeRes> selAgeRangeList();
    List<CategoryGetLevelRes> selLevelList();
    List<CategoryGetDaysRes> selDaysList();

    int insAcaAgeRange(AcademyPostReq req);
    int insAcaLevel(AcademyPostReq req);
    int insAcaDays(AcademyPostReq req);
    int insAcaTag(AcademyPostReq req);

    int insAcademy(AcademyPostReq req);
    int insTagWithAcademy(InsTagWithAcademy p);

    int updAcademy(AcademyUpdateReq req);
    int delAcademy(AcademyDeleteReq req);


// ---------------------------------------------------------------

    List<getAcademyRes> getAcademy(getAcademyReq p);
    GetAcademyDetail getAcademyDetail(Long acaId);
    List<GetTagList> getTagList();

}

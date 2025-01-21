package com.green.acamatch.academy.mapper;

import com.green.acamatch.academy.model.*;
import com.green.acamatch.academy.tag.SelTagDto;
import com.green.acamatch.academy.tag.SelTagReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AcademyMapper {

    List<SelTagDto> selTagDtoList(SelTagReq req);
    int insAcaTag(long acaId, List<Long>tagIdList);
    int delAcaTag(long acaId);

    int insAcademy(AcademyPostReq req);

    int updAcademy(AcademyUpdateReq req);
    int delAcademy(AcademyDeleteReq req);

    List<AcademyBestLikeGetRes> getAcademyBest(AcademySelOrderByLikeReq req);


// ---------------------------------------------------------------

    List<GetAcademyRes> getAcademy(GetAcademyReq p);
    GetAcademyDetail getAcademyDetail(Long acaId);
    List<GetTagList> getTagList();

}

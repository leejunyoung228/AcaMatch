package com.green.acamatch.academy.mapper;

import com.green.acamatch.academy.model.*;
import com.green.acamatch.academy.tag.SelTagDto;
import com.green.acamatch.academy.tag.SelTagReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AcademyMapper {

    List<SelTagDto> selTagDtoList(SelTagReq req);
    int insAcaTag(long acaId, List<Long>tagIdList);
    int delAcaTag(long acaId);

    int insAcademy(AcademyPostReq req);

    int updAcademy(AcademyUpdateReq req);
    Optional<String> getAcademyAddress(long acaId);
    int delAcademy(long acaId, long userId);

    List<AcademyBestLikeGetRes> getAcademyBest(AcademySelOrderByLikeReq req);


// ---------------------------------------------------------------

    List<GetAcademyRes> getAcademy(GetAcademyReq p);
    GetAcademyDetail getAcademyDetail(Long acaId);
    List<GetAcademyRes> getCategorySearch(GetCategorySearchRes p);
    int postSearch(PostAcademySearch p);
    List<GetAcademyTagDto> getTagList(Long acaId);

}

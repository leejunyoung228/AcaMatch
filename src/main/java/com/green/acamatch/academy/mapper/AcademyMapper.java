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
    int delAcademy(long acaId, long userId);

    List<AcademyBestLikeGetRes> getAcademyBest(AcademySelOrderByLikeReq req);


// ---------------------------------------------------------------
    //동과 태그를 입력받아 학원 리스트 불러오기
    List<GetAcademyRes> getAcademy(GetAcademyReq p);

    //태그를 입력받아 검색시에 search 테이블에 저장하기
    int postSearch(PostAcademySearch p);

    //학원 상세 정보 불러오기
    GetAcademyDetail getAcademyDetail(Long acaId);

    //카테고리로 학원 필터링하기
    List<GetAcademyRes> getCategorySearch(GetCategorySearchRes p);

    //학원이 등록한 태그 리스트 불러오기
    List<GetAcademyTagDto> getTagList(Long acaId);

    // 도시 리스트 가져오기
    List<GetCityRes> getCity();

    // 시/군/구 리스트 가져오기
    List<GetStreetRes> getStreet(GetStreetReq p);

    // 동 리스트 가져오기
    List<GetDongRes> getDong(GetDongReq p);

    List<GetAcademyByDongRes> getAcademyListByDong(GetAcademyByDongReq p);
}

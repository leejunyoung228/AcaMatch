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

    Long selAddressDong(String dongName);

    int insAcademy(AcademyPostReq req);

    int updAcademy(AcademyUpdateReq req);
    int insAcademyAddress(AcademyUpdateReq req);
    AcademyUpdatesGetRes selAcademyUpdatesAddress(AcademyUpdateReq req);
    int updAcademyAddress(AcademyUpdateReq req);

    AcademyUpdatesGetRes getAcademyAddress(long acaId);
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
    List<GetCategorySearchRes> getCategorySearch(GetCategorySearchReq p);

    //학원이 등록한 태그 리스트 불러오기
    List<GetAcademyTagDto> getTagList(Long acaId);

    // 도시 리스트 가져오기
    List<GetCityRes> getCity();

    // 시/군/구 리스트 가져오기
    List<GetStreetRes> getStreet(GetStreetReq p);

    // 동 리스트 가져오기
    List<GetDongRes> getDong(GetDongReq p);

    //동만 입력해서 학원 리스트 불러오기
    List<GetAcademyByDongRes> getAcademyListByDong(GetAcademyByDongReq p);

    //동과 검색어를 입력받아 학원 리스트 불러오기
    List<GetAcademyBySearchNameRes> getAcademyListBySearchName(GetAcademyBySearchNameReq p);

    //검색어만 입력받아 학원 리스트 불러오기
    List<GetAcademyByOnlySearchNameRes> getAcademyByOnlySearchName(GetAcademyByOnlySearchNameReq p);

    //검색어를 입력받아 태그 리스트 불러오기
    List<GetTagListBySearchNameRes> getTagListBySearchName(GetTagListBySearchNameReq p);

    //검색어가 없을 경우 모든 태그 리스트 출력하기
    List<GetTagListBySearchNameRes> getAllTagList();

    //userId를 입력받으면 그 유저가 등록한 학원리스트 불러오기
    List<GetAcademyListByUserIdRes> getAcademyListByUserId(GetAcademyListByUserIdReq p);

    //모든 입력을 받아 출력하기
    List<GetAcademyListRes> getAcademyListByAll(GetAcademyListReq p);

    int postToSearch(String p);

    GetAcademyDetailRes getAcademyWithClasses(GetAcademyDetailReq p);

    List<GetAcademyRandomRes> getAcademyListDefault();

    List<GetAcademyListByStudentRes> getAcademyListByStudent(GetAcademyListByStudentReq p);

    List<PopularSearchRes> popularSearch();
}

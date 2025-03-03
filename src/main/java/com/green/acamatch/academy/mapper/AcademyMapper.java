package com.green.acamatch.academy.mapper;

import com.green.acamatch.academy.model.HB.*;
import com.green.acamatch.academy.model.JW.*;
import com.green.acamatch.academy.tag.SelTagDto;
import com.green.acamatch.academy.tag.SelTagReq;
import com.green.acamatch.entity.tag.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AcademyMapper {

    List<SelTagDto> selTagDtoList(SelTagReq req);

    int insAcaTag(long acaId, List<Long>tagIdList);
    int delAcaTag(long acaId);

    Long selAddressCity(KakaoMapAddress kakaoMapAddress);
    Long selAddressStreet(KakaoMapAddress kakaoMapAddress);
    Long selAddressDong(KakaoMapAddress kakaoMapAddress);

    int insAcademy(AcademyPostReq req);

    int updAcademy(AcademyUpdateReq req);

    AcademyUpdatesGetRes getAcademyAddress(long acaId);
    int delAcademy(long acaId, long userId);

    List<AcademyBestLikeGetRes> getAcademyBest(AcademySelOrderByLikeReq req);
    AcademyBestLikeGetRes selAcademyCount();
    AcademyBestLikeGetRes selAcademyLikeCount();



// ---------------------------------------------------------------
    //동과 태그를 입력받아 학원 리스트 불러오기
    List<GetAcademyRes> getAcademy(GetAcademyReq p);

    //태그를 입력받아 검색시에 search 테이블에 저장하기
    int postSearch(PostAcademySearch p);

    //학원 상세 정보 불러오기
    GetAcademyDetail getAcademyDetail(Long acaId);

    //학원이 등록한 태그 리스트 불러오기
    List<GetAcademyTagDto> getTagList(Long acaId);

    //검색어를 입력받아 태그 리스트 불러오기
    List<GetTagListBySearchNameRes> getTagListBySearchName(GetTagListBySearchNameReq p);

    //검색어가 없을 경우 모든 태그 리스트 출력하기
    List<GetTagListBySearchNameRes> getAllTagList();

    //userId를 입력받으면 그 유저가 등록한 학원리스트 불러오기
    List<GetAcademyListByUserIdRes> getAcademyListByUserId(GetAcademyListByUserIdReq p);

    //모든 입력을 받아 출력하기
    List<GetAcademyListRes> getAcademyListByAll(GetAcademyListReq p);


    List<GetAcademyListRes> getAcademy(Long acaId);

    int postToSearch(Integer tagId);

    GetAcademyDetailRes getAcademyWithClasses(GetAcademyDetailReq p);

    List<GetAcademyRandomRes> getAcademyListRandom();

    List<GetAcademyListByStudentRes> getAcademyListByStudent(GetAcademyListByStudentReq p);

    List<PopularSearchRes> popularSearch();

    List<GetDefaultRes> getDefault(Integer size);

    GetAcademyCountRes GetAcademyCount();

    Tag getTagListByTagName(String tagName);

    List<GetAcademyInfoRes> getAcademyInfoByAcaNameClassNameExamNameAcaAgree(GetAcademyInfoReq p);

    List<GetAcademyListByDistanceRes> getAcademyListByDistance(GetAcademyListByDistanceReq p);
}

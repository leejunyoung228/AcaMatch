package com.green.acamatch.academy;

import com.green.acamatch.academy.banner.model.BannerByPositionGetRes;
import com.green.acamatch.academy.banner.model.BannerGetRes;
import com.green.acamatch.entity.academy.AcademyPic;
import com.green.acamatch.entity.academy.AcademyPicIds;
import com.green.acamatch.entity.banner.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    //배너등록 승인 업데이트
    @Transactional
    @Modifying
    @Query(" update Banner a set a.bannerType =:bannerType where a.acaId =:acaId")
    int updateBannerTypeByAcaId(Long acaId, int bannerType);

    //배너 시작일, 종료일
    @Transactional
    @Modifying
    @Query(" update Banner a set a.startDate =:startDate, a.endDate =:endDate where a.acaId =:acaId")
    int updateBannerDateByAcaId(@Param("acaId") Long acaId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    //배너 포지션에 따라 조회
    @Query("SELECT new com.green.acamatch.academy.banner.model.BannerByPositionGetRes( a.acaId, a.acaName, a.bannerType, a.startDate, a.endDate,b.bannerPicIds.bannerPic, b.bannerPosition, b.bannerShow )FROM Banner a JOIN BannerPic b ON a.acaId = b.banner.acaId WHERE a.acaId= :acaId and b.bannerPosition = :bannerPosition")
    List<BannerByPositionGetRes> findBannerByPosition( Long acaId, int bannerPosition);

    //학원 하나에 전체 배너 조회
    @Query("select new com.green.acamatch.academy.banner.model.BannerGetRes(a.acaId, a.acaName, a.bannerType, a.startDate, a.endDate,b.bannerPicIds.bannerPic, b.bannerPosition, b.bannerShow )FROM Banner a JOIN BannerPic b ON a.acaId = b.banner.acaId WHERE a.acaId= :acaId ORDER BY b.bannerPosition ASC")
    List<BannerGetRes> findBanner(Long acaId);

    //요청없이 모든 배너 조회
    @Query("select new com.green.acamatch.academy.banner.model.BannerGetRes(a.acaId, a.acaName, a.bannerType, a.startDate, a.endDate,b.bannerPicIds.bannerPic, b.bannerPosition, b.bannerShow )FROM Banner a JOIN BannerPic b ON a.acaId = b.banner.acaId ORDER BY b.bannerPosition ASC")
    List<BannerGetRes> findAllBanner();

    //배너 존재 선택


    //배너 사진 없으면 배너 데이터 삭제
    @Transactional
    @Modifying
    @Query(" delete from Banner a where a.acaId =:acaId ")
    int deleteBannerByAcaId(Long acaId);

}

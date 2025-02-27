package com.green.acamatch.academy;

import com.green.acamatch.academy.banner.model.BannerByPositionGetRes;
import com.green.acamatch.entity.academy.AcademyPic;
import com.green.acamatch.entity.academy.AcademyPicIds;
import com.green.acamatch.entity.banner.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    //배너등록 승인 업데이트
    @Transactional
    @Modifying
    @Query(" update Banner a set a.bannerType =:bannerType where a.acaId =:acaId")
    int updateBannerTypeByAcaId(Long acaId, int bannerType);

    //배너 포지션에 따라 조회
    @Query("SELECT a.acaId, a.acaName, a.bannerType, a.startDate, a.endDate, b.bannerPosition, b.bannerShow FROM Banner a JOIN BannerPic b ON a.acaId = b.acaId WHERE b.bannerPosition = :bannerPosition")
    List<BannerByPositionGetRes> findBannerByPosition( Long acaId, int bannerPosition);


}

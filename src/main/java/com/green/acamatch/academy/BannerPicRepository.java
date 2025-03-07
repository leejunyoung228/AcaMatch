package com.green.acamatch.academy;


import com.green.acamatch.academy.banner.model.BannerPicCountGetRes;
import com.green.acamatch.entity.academy.PremiumAcademy;
import com.green.acamatch.entity.banner.BannerPic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface BannerPicRepository extends JpaRepository<BannerPic, Long> {

    //Optional<BannerPic> findByBannerPic_AcaId(Long acaId);

    //배너 사진별로 비활성화/활성화
    @Transactional
    @Modifying
    @Query(" update BannerPic a set a.bannerShow =:bannerShow where a.banner.acaId =:acaId and a.bannerPosition =:bannerPosition ")
    int updateBannerPicShowByAcaIdAndBannerPosition(Long acaId, int bannerPosition, int bannerShow);

    //배너 pk 갯수 뽑기.
    @Query(" select COUNT(a) as countBannerPic from BannerPic a WHERE a.bannerPicIds.acaId = :acaId")
    Long countById(@Param("acaId") Long acaId);

    /*//배너 사진 수정
    @Transactional
    @Modifying
    @Query(" update BannerPic a set a.bannerPicIds.bannerPic =:bannerPic where a.banner.acaId =:acaId and a.bannerPosition =:bannerPosition ")
    int updateBannerPicByAcaIdAndBannerPosition(Long acaId, int bannerPosition, String bannerPic);*/

    //배너 사진 하나씩 삭제
    @Transactional
    @Modifying
    @Query(" delete from BannerPic a where a.bannerPicIds.acaId =:acaId and a.bannerPosition =:bannerPosition ")
    int deleteByacaIdAndBannerPosition(Long acaId, int bannerPosition);

}

package com.green.acamatch.academy;

import com.green.acamatch.entity.academy.AcademyPic;
import com.green.acamatch.entity.academy.AcademyPicIds;
import com.green.acamatch.entity.banner.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    //배너등록 승인 업데이트
    @Transactional
    @Modifying
    @Query(" update Banner a set a.bannerType =:bannerType where a.acaId =:acaId")
    int updateBannerTypeByAcaId(Long acaId, int bannerType);
}

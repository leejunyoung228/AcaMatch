package com.green.acamatch.academy;


import com.green.acamatch.entity.banner.BannerPic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BannerPicRepository extends JpaRepository<BannerPic, Long> {

    //배너 사진별로 비활성화/활성화
    @Transactional
    @Modifying
    @Query(" update BannerPic a set a.bannerShow =:bannerShow where a.banner.acaId =:acaId and a.bannerPosition =:bannerPosition")
    int updateBannerPicShowByAcaIdAndBannerPosition(Long acaId, int bannerPosition, int bannerShow);
}

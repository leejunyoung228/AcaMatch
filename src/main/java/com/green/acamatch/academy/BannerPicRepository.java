package com.green.acamatch.academy;


import com.green.acamatch.entity.banner.BannerPic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerPicRepository extends JpaRepository<BannerPic, Long> {
}

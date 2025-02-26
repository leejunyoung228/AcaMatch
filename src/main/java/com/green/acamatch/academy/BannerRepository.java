package com.green.acamatch.academy;

import com.green.acamatch.entity.academy.AcademyPic;
import com.green.acamatch.entity.academy.AcademyPicIds;
import com.green.acamatch.entity.banner.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Long> {

}

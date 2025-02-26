package com.green.acamatch.academy.Service;

import com.green.acamatch.academy.BannerRepository;
import com.green.acamatch.academy.PremiumRepository;
import com.green.acamatch.academy.banner.model.BannerPostReq;
import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.entity.academy.PremiumAcademy;
import com.green.acamatch.entity.banner.Banner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class BannerService {
    private final MyFileUtils myFileUtils;
    private final PremiumRepository premiumRepository;
    private final BannerRepository bannerRepository;

    public int postBanner(MultipartFile bannerPic, BannerPostReq req) {
        Banner banner = new Banner();

        String bannerPicName = (bannerPic != null ? myFileUtils.makeRandomFileName(bannerPic) : null);

        PremiumAcademy premiumAcademy = premiumRepository.findByAcademy_AcaId(req.getAcaId()).orElse(null);
        banner.getPremiumAcademy().setAcaId(premiumAcademy.getAcaId());
        banner.setBannerPic(bannerPicName);
        banner.setStartDate(null);
        banner.setEndDate(null);
        banner.setAcaName(req.getAcaName());
        banner.setBannerPosition(null);

        bannerRepository.save(banner);

        return 1;
    }
}

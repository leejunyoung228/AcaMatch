package com.green.acamatch.academy.Service;

import ch.qos.logback.classic.spi.IThrowableProxy;
import com.green.acamatch.academy.AcademyRepository;
import com.green.acamatch.academy.BannerRepository;
import com.green.acamatch.academy.PremiumRepository;
import com.green.acamatch.academy.banner.model.BannerPostReq;
import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.config.constant.AcademyConst;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.academy.PremiumAcademy;
import com.green.acamatch.entity.banner.Banner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BannerService {
    private final MyFileUtils myFileUtils;
    private final PremiumRepository premiumRepository;
    private final BannerRepository bannerRepository;
    private final AcademyRepository academyRepository;
    private final AcademyConst academyConst;

    public int postBanner(List<MultipartFile> bannerPics, BannerPostReq req) {
        log.info("req: {}", req);
        Banner banner = new Banner();

        for(MultipartFile bannerPic : bannerPics) {
            String bannerPicName = (bannerPic != null ? myFileUtils.makeRandomFileName(bannerPic) : null);
        }

        PremiumAcademy premiumAcademy = premiumRepository.findById(req.getAcaId()).orElseThrow();
        Academy academy = academyRepository.findById(req.getAcaId()).orElseThrow();


        banner.setPremiumAcademy(premiumAcademy);
        banner.setStartDate(null);
        banner.setEndDate(null);
        banner.setAcaName(academy.getAcaName());

        bannerRepository.save(banner);

        long acaId = req.getAcaId();
        String middlePath = String.format(academyConst.getBannerPicFilePath(), acaId);
        myFileUtils.makeFolders(middlePath);

        return 1;
    }
}

package com.green.acamatch.academy.Service;

import ch.qos.logback.classic.spi.IThrowableProxy;
import com.green.acamatch.academy.AcademyRepository;
import com.green.acamatch.academy.BannerRepository;
import com.green.acamatch.academy.PremiumRepository;
import com.green.acamatch.academy.banner.model.BannerPostReq;
import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.config.constant.AcademyConst;
import com.green.acamatch.config.exception.AcademyException;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.academy.PremiumAcademy;
import com.green.acamatch.entity.banner.Banner;
import com.green.acamatch.entity.banner.BannerPic;
import com.green.acamatch.entity.banner.BannerPicIds;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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

    public int postBanner(MultipartFile topBannerPic, MultipartFile bottomBannerPic,
                          MultipartFile leftBannerPic, MultipartFile rightBannerPic
                        , BannerPostReq req) {
        log.info("req: {}", req);

        long acaId = req.getAcaId();
        PremiumAcademy premiumAcademy = premiumRepository.findById(acaId).orElseThrow();
        Academy academy = academyRepository.findById(acaId).orElseThrow();

        Banner banner = new Banner();
        banner.setAcaId(acaId);
        banner.setPremiumAcademy(premiumAcademy);
        banner.setAcaName(academy.getAcaName());

        bannerRepository.save(banner);

        //배너 사진 저장
        String middlePath = String.format(academyConst.getBannerPicFilePath(), acaId);
        myFileUtils.makeFolders(middlePath);

            String topBannerPicName = (topBannerPic != null ? myFileUtils.makeRandomFileName(topBannerPic) : null);
            String bottomBannerPicName = (bottomBannerPic != null ? myFileUtils.makeRandomFileName(topBannerPic) : null);
            String leftBannerPicName = (leftBannerPic != null ? myFileUtils.makeRandomFileName(leftBannerPic) : null);
            String rightBannerPicName = (rightBannerPic != null ? myFileUtils.makeRandomFileName(rightBannerPic) : null);
            String filePath1 = String.format("%s/%s/%s", middlePath, "top", topBannerPicName);
            String filePath2 = String.format("%s/%s/%s", middlePath, "bottom", bottomBannerPicName);
            String filePath3 = String.format("%s/%s/%s", middlePath, "left", leftBannerPicName);
            String filePath4 = String.format("%s/%s/%s", middlePath, "right", rightBannerPicName);

            BannerPicIds bannerPicIds = new BannerPicIds();
            bannerPicIds.setAcaId(acaId);
            bannerPicIds.setBannerPic(topBannerPicName);

            BannerPic bannerpic = new BannerPic();
            bannerpic.setBannerPicIds(bannerPicIds);
            bannerpic.setBanner(banner);
            bannerpic.setBannerPosition(1);
            try {
                myFileUtils.transferTo(topBannerPic, filePath1);
            } catch (IOException e) {
                String delFolderPath = String.format("%s/%s", myFileUtils.getUploadPath(), middlePath);
                myFileUtils.deleteFolder(delFolderPath, true);
                throw new CustomException(AcademyException.PHOTO_SAVE_FAILED);
            }


            bannerPicIds.setAcaId(acaId);
            bannerPicIds.setBannerPic(bottomBannerPicName);

            bannerpic.setBannerPicIds(bannerPicIds);
            bannerpic.setBanner(banner);
            bannerpic.setBannerPosition(2);
            try {
                myFileUtils.transferTo(bottomBannerPic, filePath2);
            } catch (IOException e) {
                String delFolderPath = String.format("%s/%s", myFileUtils.getUploadPath(), middlePath);
                myFileUtils.deleteFolder(delFolderPath, true);
                throw new CustomException(AcademyException.PHOTO_SAVE_FAILED);
            }


            bannerPicIds.setAcaId(acaId);
            bannerPicIds.setBannerPic(leftBannerPicName);

            bannerpic.setBannerPicIds(bannerPicIds);
            bannerpic.setBanner(banner);
            bannerpic.setBannerPosition(3);
            try {
                myFileUtils.transferTo(leftBannerPic, filePath3);
            } catch (IOException e) {
                String delFolderPath = String.format("%s/%s", myFileUtils.getUploadPath(), middlePath);
                myFileUtils.deleteFolder(delFolderPath, true);
                throw new CustomException(AcademyException.PHOTO_SAVE_FAILED);
            }


            bannerPicIds.setAcaId(acaId);
            bannerPicIds.setBannerPic(rightBannerPicName);

            bannerpic.setBannerPicIds(bannerPicIds);
            bannerpic.setBanner(banner);
            bannerpic.setBannerPosition(4);
            try {
                myFileUtils.transferTo(rightBannerPic, filePath4);
            } catch (IOException e) {
                String delFolderPath = String.format("%s/%s", myFileUtils.getUploadPath(), middlePath);
                myFileUtils.deleteFolder(delFolderPath, true);
                throw new CustomException(AcademyException.PHOTO_SAVE_FAILED);
            }

        return 1;
    }
}

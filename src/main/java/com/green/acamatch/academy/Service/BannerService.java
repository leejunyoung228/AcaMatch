package com.green.acamatch.academy.Service;

import ch.qos.logback.classic.spi.IThrowableProxy;
import com.green.acamatch.academy.AcademyRepository;
import com.green.acamatch.academy.BannerPicRepository;
import com.green.acamatch.academy.BannerRepository;
import com.green.acamatch.academy.PremiumRepository;
import com.green.acamatch.academy.banner.model.BannerByPositionGetRes;
import com.green.acamatch.academy.banner.model.BannerGetRes;
import com.green.acamatch.academy.banner.model.BannerPostReq;
import com.green.acamatch.academy.banner.model.BannerUpdateReq;
import com.green.acamatch.academy.model.JW.AcademyMessage;
import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.config.constant.AcademyConst;
import com.green.acamatch.config.exception.AcademyException;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.academy.PremiumAcademy;
import com.green.acamatch.entity.banner.Banner;
import com.green.acamatch.entity.banner.BannerPic;
import com.green.acamatch.entity.banner.BannerPicIds;
import com.green.acamatch.entity.myenum.BannerPosition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.service.RequestBodyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BannerService {
    private final MyFileUtils myFileUtils;
    private final PremiumRepository premiumRepository;
    private final BannerRepository bannerRepository;
    private final BannerPicRepository bannerPicRepository;
    private final AcademyRepository academyRepository;
    private final AcademyConst academyConst;
    private final RequestBodyService requestBodyBuilder;
    private final AcademyMessage academyMessage;

    //배너신청
    @Transactional
    public int postBanner(MultipartFile topBannerPic, MultipartFile bottomBannerPic
                        , MultipartFile rightBannerPic
                        , BannerPostReq req) {
        log.info("req: {}", req);

        //특정 학원pk가 하나라도 있을때
        if(bannerPicRepository.countById(req.getAcaId()) > 0) {
            throw new CustomException(AcademyException.DATA_EXISTS);
        }

        //배너사진을 하나도 넣지 않았을때
        if((topBannerPic == null && bottomBannerPic == null && rightBannerPic == null)) {
            throw new CustomException(AcademyException.MISSING_REQUIRED_FILED_EXCEPTION);
        }

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

        myFileUtils.deleteFolder(String.format("%s/%s", myFileUtils.getUploadPath(), middlePath), true);


            String topBannerPicName = (topBannerPic != null ? myFileUtils.makeRandomFileName(topBannerPic) : null);
            String bottomBannerPicName = (bottomBannerPic != null ? myFileUtils.makeRandomFileName(topBannerPic) : null);
            String rightBannerPicName = (rightBannerPic != null ? myFileUtils.makeRandomFileName(rightBannerPic) : null);

            String filePath1 = String.format("%s/%s/%s", middlePath, "top", topBannerPicName);
            myFileUtils.makeFolders(filePath1);
            String filePath2 = String.format("%s/%s/%s", middlePath, "bottom", bottomBannerPicName);
            myFileUtils.makeFolders(filePath2);
            String filePath4 = String.format("%s/%s/%s", middlePath, "right", rightBannerPicName);
            myFileUtils.makeFolders(filePath4);

            BannerPicIds bannerPicIds = new BannerPicIds();
            bannerPicIds.setAcaId(acaId);
            bannerPicIds.setBannerPic(topBannerPicName);

            BannerPic bannerpic = new BannerPic();
            bannerpic.setBannerPicIds(bannerPicIds);
            bannerpic.setBanner(banner);
            bannerpic.setBannerPosition(1);

                bannerPicRepository.save(bannerpic);


            if(topBannerPic != null && !topBannerPic.isEmpty()) {
                try {
                    myFileUtils.transferTo(topBannerPic, filePath1);
                } catch (IOException e) {
                    String delFolderPath = String.format("%s/%s", myFileUtils.getUploadPath(), middlePath);
                    myFileUtils.deleteFolder(delFolderPath, true);
                    throw new CustomException(AcademyException.PHOTO_SAVE_FAILED);
                }
            }


            bannerPicIds.setAcaId(acaId);
            bannerPicIds.setBannerPic(bottomBannerPicName);

            bannerpic.setBannerPicIds(bannerPicIds);
            bannerpic.setBanner(banner);
            bannerpic.setBannerPosition(2);

                bannerPicRepository.save(bannerpic);


            if(bottomBannerPic != null && !bottomBannerPic.isEmpty()) {
                try {
                    myFileUtils.transferTo(bottomBannerPic, filePath2);
                } catch (IOException e) {
                    String delFolderPath = String.format("%s/%s", myFileUtils.getUploadPath(), middlePath);
                    myFileUtils.deleteFolder(delFolderPath, true);
                    throw new CustomException(AcademyException.PHOTO_SAVE_FAILED);
                }
            }



        bannerPicIds.setAcaId(acaId);
        bannerPicIds.setBannerPic(rightBannerPicName);

        bannerpic.setBannerPicIds(bannerPicIds);
        bannerpic.setBanner(banner);
        bannerpic.setBannerPosition(4);

            bannerPicRepository.save(bannerpic);


        if(rightBannerPic != null && !rightBannerPic.isEmpty()) {
            try {
                myFileUtils.transferTo(rightBannerPic, filePath4);
            } catch (IOException e) {
                String delFolderPath = String.format("%s/%s", myFileUtils.getUploadPath(), middlePath);
                myFileUtils.deleteFolder(delFolderPath, true);
                throw new CustomException(AcademyException.PHOTO_SAVE_FAILED);
            }
        }

        academyMessage.setMessage("배너신청이 완료되었습니다.");
        return 1;
    }

    //배너승인
    @Transactional
    public int updateBannerType(Long acaId, int bannerType) {
        int result = bannerRepository.updateBannerTypeByAcaId(acaId, bannerType);
        if(result == 1) {
            bannerRepository.updateBannerDateByAcaId(acaId, LocalDate.now(), LocalDate.now().plusMonths(1));
        }
        academyMessage.setMessage("배너 승인이 완료되었습니다.");
        return 1;
    }


    //배너 활성화/비활성화
    @Transactional
    public int updateBannerShow(Long acaId, int bannerPosition, int bannerShow) {
        bannerPicRepository.updateBannerPicShowByAcaIdAndBannerPosition(acaId, bannerPosition, bannerShow);
        if(bannerShow == 1) {
            academyMessage.setMessage("배너가 활성화 되었습니다.");
        }else {
            academyMessage.setMessage("배너가 비활성화 되었습니다.");
        }
        return 1;
    }

    //배너 포지션별 조회
    @Transactional
    public List<BannerByPositionGetRes> getBannerByPosition(Long acaId, int position) {
        List<BannerByPositionGetRes> res = bannerRepository.findBannerByPosition(acaId, position);
        academyMessage.setMessage("배너가 조회되었습니다.");
        return res;
    }

    //특정 프리미엄학원의 배너 조회
    @Transactional
    public List<BannerGetRes> getBanner(Long acaId) {
        List<BannerGetRes> res = bannerRepository.findBanner(acaId);
        academyMessage.setMessage("%d번, acaId" + " 프리미엄학원의 배너가 조회되었습니다.");
        return res;
    }

    //프리미엄 학원의 모든 배너 조회
    @Transactional
    public List<BannerGetRes> getBannerAll() {
        List<BannerGetRes> res = bannerRepository.findAllBanner();
        academyMessage.setMessage("프리미엄학원의 모든 배너가 조회되었습니다.");
        return res;
    }


    //배너 사진 수정
    @Transactional
    public int updBanner(MultipartFile pic, BannerUpdateReq req) {
        Optional<BannerPic> bannerPicOptional = bannerPicRepository.findById(req.getAcaId());

        if(!bannerPicOptional.isPresent()) {
            throw new CustomException(AcademyException.NOT_FOUND_BANNER);
        }
        BannerPic bannerPic = bannerPicOptional.get();

        //배너사진을 하나도 넣지 않았을때 예외처리
        /*if(pic == null ) {
            throw new CustomException(AcademyException.MISSING_REQUIRED_FILED_EXCEPTION);
        }*/

        Long acaId = req.getAcaId();

        //배너 사진 저장
        String middlePath = String.format(academyConst.getBannerPicFilePath(), acaId);
        myFileUtils.deleteFolder(String.format("%s/%s", myFileUtils.getUploadPath(), middlePath), true);

        String BannerPicName = myFileUtils.makeRandomFileName(pic);

        String filePath;
        if(req.getBannerPosition() == 1) {
            filePath = String.format("%s/%s/%s", middlePath, "top", BannerPicName);
            myFileUtils.makeFolders(filePath);
        } else if(req.getBannerPosition() == 2) {
            filePath = String.format("%s/%s/%s", middlePath, "bottom", BannerPicName);
            myFileUtils.makeFolders(filePath);
        } else if(req.getBannerPosition() == 4) {
            filePath = String.format("%s/%s/%s", middlePath, "right", BannerPicName);
            myFileUtils.makeFolders(filePath);
        } else {
            throw new CustomException(AcademyException.NOT_FOUND_BANNER);
        }
        BannerPicIds bannerPicIds = new BannerPicIds();
        bannerPicIds.setAcaId(acaId);
        bannerPicIds.setBannerPic(BannerPicName);

        bannerPic.setBannerPicIds(bannerPicIds);
        //bannerPicRepository.save(bannerPic);

        bannerPicRepository.updateBannerPicByAcaIdAndBannerPosition(acaId, req.getBannerPosition(), BannerPicName);

            try {
                myFileUtils.transferTo(pic, filePath);
            } catch (IOException e) {
                String delFolderPath = String.format("%s/%s", myFileUtils.getUploadPath(), middlePath);
                myFileUtils.deleteFolder(delFolderPath, true);
                throw new CustomException(AcademyException.PHOTO_SAVE_FAILED);
            }

        academyMessage.setMessage("배너를 수정하였습니다.");
        return 1;
    }



    //배너 하나씩 삭제
    @Transactional
    public int delBanner(Long acaId, int bannerPosition) {
        bannerPicRepository.deleteByacaIdAndBannerPosition(acaId, bannerPosition);
        if(bannerPicRepository.countById(acaId) == 0) {
            bannerRepository.deleteBannerByAcaId(acaId);
        }

        academyMessage.setMessage("배너가 삭제되었습니다.");
        return 1;
    }
}

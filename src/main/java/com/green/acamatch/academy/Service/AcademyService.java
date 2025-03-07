package com.green.acamatch.academy.Service;

import com.green.acamatch.academy.AcademyPicRepository;
import com.green.acamatch.academy.AcademyRepository;
import com.green.acamatch.academy.mapper.AcademyMapper;
import com.green.acamatch.academy.model.*;
import com.green.acamatch.academy.model.HB.*;
import com.green.acamatch.academy.model.JW.*;

import com.green.acamatch.academy.tag.AcademyTagRepository;
import com.green.acamatch.academy.tag.SearchRepository;
import com.green.acamatch.academy.tag.TagRepository;
import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.config.constant.AcademyConst;
import com.green.acamatch.config.constant.AddressConst;
import com.green.acamatch.config.exception.AcademyException;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.security.AuthenticationFacade;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.academy.AcademyPic;
import com.green.acamatch.entity.academy.AcademyPicIds;
import com.green.acamatch.entity.tag.Search;
import com.green.acamatch.entity.tag.Tag;
import com.green.acamatch.entity.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AcademyService {
    private final AcademyMapper academyMapper;
    private final AcademyConst academyConst;
    private final MyFileUtils myFileUtils;
    private final AcademyMessage academyMessage;
    private final TagService tagService;
    private final AddressConst addressConst;
    private final AcademyRepository academyRepository;
    private final AcademyPicRepository academyPicRepository;
    private final SearchRepository searchRepository;
    private final AcademyTagRepository academyTagRepository;
    private final TagRepository tagRepository;


    //학원정보등록
    @Transactional
    public int insAcademy(List<MultipartFile> pics, MultipartFile businessPic, MultipartFile operationLicensePic, AcademyPostReq req) {
        /*List<AcademyPic> academyPics = new ArrayList<>(pics.size());
        for (MultipartFile pic : pics ) {
            academyPics.add();

        } 3차에 참고
        academyPicRepository.saveAll(academyPics);*/

        User signedUser = new User();
        signedUser.setUserId(AuthenticationFacade.getSignedUserId());


        if (req.getTagNameList().isEmpty()) {

            throw new CustomException(AcademyException.MISSING_REQUIRED_FILED_EXCEPTION);
        }


        //기본주소를 통해 지번(동)이름 가져오는 api 메소드 호출
        KakaoMapAddress kakaoMapAddressImp = KakaoApiExample.addressSearchMain(req.getAddress());

        // 가져온 지번(시) 이름과 매칭되는 시 pk 번호를 select
        Long cityPk = academyMapper.selAddressCity(kakaoMapAddressImp);
        kakaoMapAddressImp.setCityId(cityPk);
        // 가져온 지번(구) 이름과 매칭되는 구 pk 번호를 select
        Long streetPk = academyMapper.selAddressStreet(kakaoMapAddressImp);
        kakaoMapAddressImp.setStreetId(streetPk);
        // 가져온 지번(동) 이름과 매칭되는 동 pk 번호를 select
        Long dongPk = academyMapper.selAddressDong(kakaoMapAddressImp);

        req.setDongId(dongPk);


        //기본주소를 통해 위도, 경도 가져오는 api 메소드 호출
        KakaoMapAddress kakaoMapAddressXY = KakaoApiExample.addressXY(req.getAddress());
        req.setLon(kakaoMapAddressXY.getLongitude());
        req.setLat(kakaoMapAddressXY.getLatitude());

        //사업자등록번호 존재여부 api 메소드 호출
        BusinessApiNumber businessApiNumber = BusinessNumberValidation.isBusinessNumberValid(req.getBusinessNumber());

        if (businessApiNumber == null || !businessApiNumber.isvalid()) {
            throw new CustomException(AcademyException.NOT_FOUND_BUSINESSNUMBER);
        }



        String businessPicName = (businessPic != null ? myFileUtils.makeRandomFileName(businessPic) : null);
        String operationLicensePicName = (operationLicensePic != null ? myFileUtils.makeRandomFileName(businessPic) : null);

        //@@ academyMapper.insAcademy(req);
        Academy academy = new Academy();
        academy.setUser(signedUser);
        academy.setDongId(req.getDongId());
        academy.setAcaName(req.getAcaName());
        academy.setAcaPhone(req.getAcaPhone());
        academy.setComment(req.getComment());
        academy.setTeacherNum(req.getTeacherNum());
        academy.setOpenTime(req.getOpenTime());
        academy.setCloseTime(req.getCloseTime());
        academy.setAddress(req.getAddress());
        academy.setDetailAddress(req.getDetailAddress());
        academy.setPostNum(req.getPostNum());
        academy.setLat(req.getLat());
        academy.setLon(req.getLon());
        academy.setBusinessName(req.getBusinessName());
        academy.setBusinessNumber(req.getBusinessNumber());
        academy.setBusinessPic(businessPicName);
        academy.setOperationLicencePic(operationLicensePicName);
        academy.setCorporateNumber(req.getCorporateNumber());
        academyRepository.save(academy);

        long acaId = academy.getAcaId();
        String middlePath = String.format(academyConst.getAcademyPicFilePath(), acaId);
        myFileUtils.makeFolders(middlePath);

        String middlePath2 = String.format(academyConst.getBusinessLicenseFilePath(), acaId);
        myFileUtils.makeFolders(middlePath2);

        String middlePath3 = String.format(academyConst.getOperationLicenseFilePath(), acaId);
        myFileUtils.makeFolders(middlePath3);

        // 사업자등록증
        //TODO 중복 코드 메소드 분리
        String filePath2 = String.format("%s/%s", middlePath2, businessPicName);

        try {
            myFileUtils.transferTo(businessPic, filePath2);
        } catch (IOException e) {
            String delFolderPath = String.format("%s/%s", myFileUtils.getUploadPath(), middlePath2);
            myFileUtils.deleteFolder(delFolderPath, true);
            throw new CustomException(AcademyException.PHOTO_SAVE_FAILED);
        }

        // 학원운영증
        String filePath3 = String.format("%s/%s", middlePath3, operationLicensePicName);

        try {
            myFileUtils.transferTo(operationLicensePic, filePath3);
        } catch (IOException e) {
            String delFolderPath = String.format("%s/%s", myFileUtils.getUploadPath(), middlePath3);
            myFileUtils.deleteFolder(delFolderPath, true);
            throw new CustomException(AcademyException.PHOTO_SAVE_FAILED);
        }

        if (pics == null || pics.isEmpty()) {
            academyMessage.setMessage("학원정보등록이 완료되었습니다.");
            return 1;
        }


        // 학원 사진들
        List<String> picNameList = new ArrayList<>();
        List<AcademyPic> picList = new ArrayList<>(pics.size());
        for (MultipartFile pic : pics) {
            String savedPicName = (pic != null ? myFileUtils.makeRandomFileName(pic) : null);
            picNameList.add(savedPicName);
            String filePath = String.format("%s/%s", middlePath, savedPicName);

            AcademyPicIds academyPicIds = new AcademyPicIds();
            academyPicIds.setAcaId(acaId);
            academyPicIds.setAcaPic(savedPicName);

            AcademyPic academyPic = new AcademyPic();
            academyPic.setAcademyPicIds(academyPicIds);
            academyPic.setAcademy(academy);
            picList.add(academyPic);
            try {
                myFileUtils.transferTo(pic, filePath);
            } catch (IOException e) {
                String delFolderPath = String.format("%s/%s", myFileUtils.getUploadPath(), middlePath);
                myFileUtils.deleteFolder(delFolderPath, true);
                throw new CustomException(AcademyException.PHOTO_SAVE_FAILED);
            }
        }
        academyPicRepository.saveAll(picList);




        /* AcademyPicDto academyPicDto = new AcademyPicDto();
        academyPicDto.setAcaId(acaId);
        academyPicDto.setPics(picNameList);*/ //2차때 사용함

        ////academyPicsMapper.insAcademyPics(academyPicDto); 2차때 사용함.
        List<Tag> tagList = tagService.insTag(req.getTagNameList());
        tagService.insAcaTag(academy, tagList);

        academyMessage.setMessage("학원정보등록이 완료되었습니다.");
        return 1;
    }

    //학원정보수정
    @Transactional
    public int updAcademy(List<MultipartFile> pics, AcademyUpdateReq req) {
        //아무것도 입력안했을 때 예외처리.
        if ((pics == null || pics.toString().trim().isEmpty()) &&
                (req.getAcaName() == null || req.getAcaName().trim().isEmpty()) &&
                (req.getAcaPhone() == null || req.getAcaPhone().trim().isEmpty()) &&
                (req.getComment() == null || req.getComment().trim().isEmpty()) &&
                (req.getTeacherNum() == null) && // int 타입은 null 체크 불필요
                (req.getOpenTime() == null) &&
                (req.getCloseTime() == null) &&
                (req.getAddress() == null || req.getAddress().trim().isEmpty()) &&
                (req.getDetailAddress() == null || req.getDetailAddress().trim().isEmpty()) &&
                (req.getPostNum() == null || req.getPostNum().trim().isEmpty()) &&
                (req.getTagNameList() == null || req.getTagNameList().isEmpty()))
        {
            throw new CustomException(AcademyException.MISSING_UPDATE_FILED_EXCEPTION);
        }

        Academy academy = academyRepository.findById(req.getAcaId()).orElseThrow(() -> new CustomException(AcademyException.NOT_FOUND_ACADEMY));
        long acaId = academy.getAcaId();

        if (req.getAcaName() != null) academy.setAcaName(req.getAcaName());
        if (req.getAcaPhone() != null) academy.setAcaPhone(req.getAcaPhone());
        if (req.getComment() != null) academy.setComment(req.getComment());
        if (req.getTeacherNum() != null) academy.setTeacherNum(req.getTeacherNum());
        if (req.getOpenTime() != null) academy.setOpenTime(req.getOpenTime());
        if (req.getCloseTime() != null) academy.setCloseTime(req.getCloseTime());
        if (req.getAddress() != null) {
            academy.setAddress(req.getAddress());
            //기본주소를 통해 지번(동)이름 가져오는 api 메소드 호출
            KakaoMapAddress kakaoMapAddressImp = KakaoApiExample.addressSearchMain(req.getAddress());

            // 가져온 지번(시) 이름과 매칭되는 시 pk 번호를 select
            Long cityPk = academyMapper.selAddressCity(kakaoMapAddressImp);
            kakaoMapAddressImp.setCityId(cityPk);
            // 가져온 지번(구) 이름과 매칭되는 구 pk 번호를 select
            Long streetPk = academyMapper.selAddressStreet(kakaoMapAddressImp);
            kakaoMapAddressImp.setStreetId(streetPk);
            // 가져온 지번(동) 이름과 매칭되는 동 pk 번호를 select
            Long dongPk = academyMapper.selAddressDong(kakaoMapAddressImp);
            academy.setDongId(dongPk);

            //기본주소를 통해 위도, 경도 가져오는 api 메소드 호출
            KakaoMapAddress kakaoMapAddressXY = KakaoApiExample.addressXY(req.getAddress());
            academy.setLon(kakaoMapAddressXY.getLongitude());
            academy.setLat(kakaoMapAddressXY.getLatitude());
        }
        if (req.getDetailAddress() != null) academy.setDetailAddress(req.getDetailAddress());
        if (req.getPostNum() != null) academy.setPostNum(req.getPostNum());
        if (req.getAcaAgree() != null) academy.setAcaAgree(req.getAcaAgree());
        if (req.getPremium() != null) academy.setPremium(req.getPremium());
        if (req.getLat() != null) academy.setLat(req.getLat());
        if (req.getLon() != null) academy.setLon(req.getLon());

        //기본주소를 통해 지번(동)이름 가져오는 api 메소드 호출
        KakaoMapAddress kakaoMapAddressImp = KakaoApiExample.addressSearchMain(req.getAddress());

        // 가져온 지번(시) 이름과 매칭되는 시 pk 번호를 select
        Long cityPk = academyMapper.selAddressCity(kakaoMapAddressImp);
        kakaoMapAddressImp.setCityId(cityPk);
        // 가져온 지번(구) 이름과 매칭되는 구 pk 번호를 select
        Long streetPk = academyMapper.selAddressStreet(kakaoMapAddressImp);
        kakaoMapAddressImp.setStreetId(streetPk);
        // 가져온 지번(동) 이름과 매칭되는 동 pk 번호를 select
        Long dongPk = academyMapper.selAddressDong(kakaoMapAddressImp);

        req.setDongId(dongPk);


        //기본주소를 통해 위도, 경도 가져오는 api 메소드 호출
        KakaoMapAddress kakaoMapAddressXY = KakaoApiExample.addressXY(req.getAddress());
        req.setLon(kakaoMapAddressXY.getLongitude());
        req.setLat(kakaoMapAddressXY.getLatitude());


        //학원사진수정
        if (pics != null && !pics.isEmpty()) {
            academyPicRepository.deleteAcademyPicsByAcaId(acaId);
            String middlePath = String.format(academyConst.getAcademyPicFilePath(), acaId);
            myFileUtils.deleteFolder(middlePath, false);
            myFileUtils.makeFolders(middlePath);


            List<String> picNameList = new ArrayList<>();
            List<AcademyPic> picList = new ArrayList<>(pics.size());
            for (MultipartFile pic : pics) {
                String savedPicName = (pic != null ? myFileUtils.makeRandomFileName(pic) : null);

                picNameList.add(savedPicName);
                String filePath = String.format("%s/%s", middlePath, savedPicName);

                AcademyPicIds academyPicIds = new AcademyPicIds();
                academyPicIds.setAcaId(acaId);
                academyPicIds.setAcaPic(savedPicName);

                AcademyPic academyPic = new AcademyPic();
                academyPic.setAcademyPicIds(academyPicIds);
                academyPicRepository.save(academyPic);
                /*academyPic.setAcademyPicIds(academyPicIds);
                academyPic.setAcademy(academy);
                picList.add(academyPic);*/

                try {
                    myFileUtils.transferTo(pic, filePath);
                } catch (IOException e) {
                    String delFolderPath = String.format("%s/%s", myFileUtils.getUploadPath(), middlePath);
                    myFileUtils.deleteFolder(delFolderPath, false);
                    throw new CustomException(AcademyException.PHOTO_SAVE_FAILED);
                }
            }

            if (req.getTagNameList() != null) {
                academyTagRepository.deleteAllByAcademy_AcaId(req.getAcaId());
                tagService.insAcaTag(academy, tagService.insTag(req.getTagNameList()));
            }
            //academyPicRepository.saveAll(picList);
        }
        ;

        academyMessage.setMessage("학원정보수정이 완료되었습니다.");

        academyRepository.save(academy);
        return 1;
    }

    private String saveLicensePic(String middlePath, MultipartFile file) {
        myFileUtils.deleteFolder(middlePath, false);

        String savedPicName = myFileUtils.makeRandomFileName(file);
        String filePath = String.format("%s/%s", middlePath, savedPicName);

        try {
            myFileUtils.transferTo(file, filePath);
        } catch (IOException e) {
            String delFolderPath = String.format("%s/%s", myFileUtils.getUploadPath(), middlePath);
            myFileUtils.deleteFolder(delFolderPath, false);
            throw new CustomException(AcademyException.PHOTO_SAVE_FAILED);
        }

        return savedPicName;
    }



        /*//아무것도 입력안했을 때
//        if ((pics == null || pics.toString().trim().isEmpty()) &&
//                (req.getAcaName() == null || req.getAcaName().trim().isEmpty()) &&
//                (req.getAcaPhone() == null || req.getAcaPhone().trim().isEmpty()) &&
//                (req.getComment() == null || req.getComment().trim().isEmpty()) &&
//                (req.getTeacherNum() == 0) && // int 타입은 null 체크 불필요
//                (req.getOpenTime() == null || req.getOpenTime().trim().isEmpty()) &&
//                (req.getCloseTime() == null || req.getCloseTime().trim().isEmpty()) &&
//                (req.getAddress() == null || req.getAddress().trim().isEmpty()) &&
//                (req.getDetailAddress() == null || req.getDetailAddress().trim().isEmpty()) &&
//                (req.getPostNum() == null || req.getPostNum().trim().isEmpty()) &&
//                (req.getAcaAgree() == 0) &&
//                (req.getTagIdList() == null || req.getTagIdList().isEmpty()) &&
//                (req.getPremium() == 0) &&
//                (req.getLat() == 0.0) &&
//                (req.getLon() == 0.0) &&
//                (req.getBusinessName() == null || req.getBusinessName().isEmpty()) &&
//                (req.getBusinessNumber() == null || req.getBusinessNumber().isEmpty()) &&
//                (req.getBusinessPic()== null || req.getBusinessPic().isEmpty()) &&
//                (req.getOperationLicencePic() == null || req.getOperationLicencePic().isEmpty()) &&
//                (req.getCorporateNumber() == null || req.getCorporateNumber().isEmpty())) {
//            throw new CustomException(AcademyException.MISSING_UPDATE_FILED_EXCEPTION);
//        }

        User signedUser = new User();
        signedUser.setUserId(authenticationFacade.getSignedUserId());

        long acaId = req.getAcaId();
        String middlePath = String.format("academy/%d", acaId);
        myFileUtils.makeFolders(middlePath);

        String middlePath2 = String.format("businessLicence/%d", acaId);
        myFileUtils.makeFolders(middlePath2);

        String middlePath3 = String.format("operationLicence/%d", acaId);
        myFileUtils.makeFolders(middlePath3);
        // 프로필 사진 처리
        *//*if (pics != null && !pics.isEmpty()) {
            String targetDir = String.format("%s/%d", "academy", req.getAcaId());
            myFileUtils.makeFolders(targetDir);

            String savedFileName = myFileUtils.makeRandomFileName(pic);
            req.setAcaPic(savedFileName);

            // 기존 파일 삭제
            String deletePath = String.format("%s/academy/%d", myFileUtils.getUploadPath(), req.getAcaId());
            myFileUtils.deleteFolder(deletePath, false);

            // 파일 저장
            String filePath = String.format("%s/%s", targetDir, savedFileName);

            try {
                myFileUtils.transferTo(pics, filePath);
            } catch (IOException e) {
                throw new CustomException(AcademyException.PHOTO_SAVE_FAILED);
            }
        }*//* //2차때 사용함.


        //학원사진을 수정할때
        if(pics != null && !pics.isEmpty()) {
            //기존에 사진 삭제
            int affecteRows = academyPicRepository.deleteAcademyPicsByAcaId(req.getAcaId());
            log.info("accectedRows: {}", affecteRows);

            //새로운 사진 삽입
            List<String> picNameList = new ArrayList<>();
            for (MultipartFile pic : pics) {
                String savedPicName = (pic != null ? myFileUtils.makeRandomFileName(pic) : null);
                picNameList.add(savedPicName);
                String filePath = String.format("%s/%s", middlePath, savedPicName);


                try {
                    Academy academy = new Academy();

                    AcademyPicIds academyPicIds = new AcademyPicIds();
                    academyPicIds.setAcaId(acaId);
                    academyPicIds.setAcaPic(savedPicName);

                    AcademyPic academyPic = new AcademyPic();
                    academyPic.setAcademyPicIds(academyPicIds);
                    academyPic.setAcademy(academy);

                    academyPicRepository.save(academyPic);

                    myFileUtils.transferTo(pic, filePath);
                } catch (IOException e) {
                    String delFolderPath = String.format("%s/%s", myFileUtils.getUploadPath(), middlePath);
                    myFileUtils.deleteFolder(delFolderPath, true);
                    throw new CustomException(AcademyException.PHOTO_SAVE_FAILED);
                }
            }
        }

        //사업자등록증 수정할 때


        //학원운영증 수정할 때

        //전화번호 형식 무시할수 있게
        if (req.getAcaPhone() == null || req.getAcaPhone().trim().isEmpty()) {
            // acaPhone이 빈 값일 경우에는 유효성 검사를 건너뛴다.
            req.setAcaPhone(null);
        } else {
            // acaPhone이 비어 있지 않으면 유효성 검사 로직이 진행된다.
            if (!req.getAcaPhone().matches("^(0[0-9][0-9])-\\d{3,4}-\\d{4}$")) {
                throw new CustomException(AcademyException.MISSING_REQUIRED_FILED_EXCEPTION);
            }
        }

        //주소수정을 하려고 할때(셋다 값이 들어있을때)
        if (isValidValue(req.getAddress())
                && isValidValue(req.getDetailAddress())
                && isValidValue(req.getPostNum())) {

            //기본주소를 통해 지번(동)이름 가져오는 api 메소드 호출
            KakaoMapAddress kakaoMapAddressImp = kakaoApiExample.addressSearchMain(req.getAddress());

            // 가져온 지번(시) 이름과 매칭되는 시 pk 번호를 select
            Long cityPk = academyMapper.selAddressCity(kakaoMapAddressImp);
            kakaoMapAddressImp.setCityId(cityPk);
            // 가져온 지번(구) 이름과 매칭되는 구 pk 번호를 select
            Long streetPk = academyMapper.selAddressStreet(kakaoMapAddressImp);
            kakaoMapAddressImp.setStreetId(streetPk);
            // 가져온 지번(동) 이름과 매칭되는 동 pk 번호를 select
            Long dongPk = academyMapper.selAddressDong(kakaoMapAddressImp);

            req.setDongId(dongPk);


            String address = req.getAddress();
            String detailAddress = req.getDetailAddress();
            String postNum = req.getPostNum();

            int emptyCount = 0;
            if (address == null || address.trim().isEmpty()) {
                emptyCount++;
            }
            if (detailAddress == null || detailAddress.trim().isEmpty()) {
                emptyCount++;
            }
            if (postNum == null || postNum.trim().isEmpty()) {
                emptyCount++;
            }
            // 3개 중 하나 또는 두 개가 비어있으면 예외를 발생시킴
            if (emptyCount > 0 && emptyCount < 3) {
                throw new CustomException(AcademyException.ILLEGAL_ARGUMENT_EXCEPTION);
            }
        }


        //태그만 값을 가질때
        if ( (req.getTagIdList() != null && !req.getTagIdList().isEmpty()) &&
                (req.getAcaName() == null || req.getAcaName().isEmpty()) &&
                (req.getAcaPhone() == null || req.getAcaPhone().isEmpty()) &&
                (req.getComment() == null || req.getComment().isEmpty()) &&
                req.getTeacherNum() == 0 &&
                (req.getOpenTime() == null || req.getOpenTime().isEmpty()) &&
                (req.getCloseTime() == null || req.getCloseTime().isEmpty()) &&
                (req.getAddress() == null || req.getAddress().isEmpty()) &&
                (req.getDetailAddress() == null || req.getDetailAddress().isEmpty()) &&
                (req.getPostNum() == null || req.getPostNum().isEmpty()) &&
                (req.getAcaAgree() == 0) &&
                (req.getLat() == 0.0) &&
                (req.getLon() == 0.0) &&
                (req.getBusinessName() == null || req.getBusinessName().isEmpty()) &&
                (req.getBusinessNumber() == null || req.getBusinessNumber().isEmpty()) &&
                (req.getBusinessPic() == null || req.getBusinessPic().isEmpty()) &&
                (pics == null || pics.isEmpty()) &&
                (req.getOperationLicencePic() == null || req.getOperationLicencePic().isEmpty()) &&
                (req.getCorporateNumber() == null || req.getCorporateNumber().isEmpty()) )
        {

            try {
                //academyMapper.delAcaTag(req.getAcaId());
                tagService.delAcaTag(req.getAcaId());
                //academyMapper.insAcaTag(req.getAcaId(), req.getTagIdList());
                tagService.insAcaTag(academyRepository.findById(req.getAcaId()).orElseThrow(() -> new CustomException(AcademyException.NOT_FOUND_ACADEMY)), tagRepository.findAllById(req.getTagIdList()));

            } catch (DataIntegrityViolationException e) {
                throw new CustomException(AcademyException.DUPLICATE_TAG);
            }

            academyMessage.setMessage("학원정보수정이 완료되었습니다.");
            return 1;
        }

        int result = academyMapper.updAcademy(req);


        if (result == 0) {
            academyMessage.setMessage("학원정보수정을 실패하였습니다.");
            return result;
        }

        if (req.getTagIdList() != null && !req.getTagIdList().isEmpty()) {
            try {
                academyMapper.delAcaTag(req.getAcaId());
                academyMapper.insAcaTag(req.getAcaId(), req.getTagIdList());

            } catch (DataIntegrityViolationException e) {
                throw new CustomException(AcademyException.DUPLICATE_TAG);
            }
        }
        academyMessage.setMessage("학원정보수정이 완료되었습니다.");
        return result;*/


    //학원정보삭제
    public int delAcademy(AcademyDeleteReq req) {
        //academyMapper.delAcaTag(req.getAcaId());
        //int result = academyMapper.delAcademy(req.getAcaId(), req.getUserId());

        int result = academyRepository.deleteAcademyByAcaIdAndUserId(req.getAcaId(), req.getUserId());

        if (result == 1) {
            academyMessage.setMessage("학원정보가 삭제되었습니다.");
            return result;
        } else {
            academyMessage.setMessage("학원정보 삭제가 실패하였습니다.");
            return result;
        }
    }

    //학원정보등록 승인(관리자)
    public int updAcademyAgree(AcademyAgreeUpdReq req) {
        int result = academyRepository.updateAcademyAcaAgreeByAcaId(req.getAcaId());
        academyMessage.setMessage("학원정보등록이 승인되었습니다.");
        return result;
    }

    //학원좋아요순
    public List<AcademyBestLikeGetRes> getAcademyBest(AcademySelOrderByLikeReq req) {
        List<AcademyBestLikeGetRes> list = academyMapper.getAcademyBest(req);

        AcademyBestLikeGetRes academyLikeCountRes = academyMapper.selAcademyLikeCount();

        for (AcademyBestLikeGetRes academy : list) {
            academy.setAcademyLikeCount(academyLikeCountRes.getAcademyLikeCount());
        }

        if (list == null || list.isEmpty()) {
            academyMessage.setMessage("좋아요를 받은 학원이 없습니다.");
            return null;
        }
        academyMessage.setMessage("좋아요를 많이 받은 학원 순서입니다.");
        return list;
    }


    //주소 인코딩
    public String addressEncoding(AddressDto addressDto) {
        return addressDto.getAddress() +
                addressConst.getStartSep() + addressConst.getDetailAddressSep()
                + addressDto.getDetailAddress() +
                addressConst.getEndSep() + addressConst.getDetailAddressSep() +

                addressConst.getStartSep() + addressConst.getPostNumSep()
                + addressDto.getPostNum() +
                addressConst.getEndSep() + addressConst.getPostNumSep();
    }

    //주소 디코딩
    public AddressDto addressDecoding(String address) {
        AddressDto res = new AddressDto();
        res.setAddress(address.substring(0, address.indexOf(addressConst.getStartSep())));
        res.setDetailAddress(
                address.substring(
                        address.indexOf(addressConst.getStartSep() + addressConst.getDetailAddressSep())
                                + addressConst.getStartSep().length() + addressConst.getDetailAddressSep().length(),
                        address.indexOf(addressConst.getEndSep() + addressConst.getDetailAddressSep())));
        res.setPostNum(
                address.substring(
                        address.indexOf(addressConst.getStartSep() + addressConst.getPostNumSep())
                                + addressConst.getStartSep().length() + addressConst.getPostNumSep().length(),
                        address.indexOf(addressConst.getEndSep() + addressConst.getPostNumSep())
                ));
        return res;
    }

    //빈값인지 확인하는 메소드(String, int, long )
    private boolean isValidValue(Object value) {
        if (value instanceof String) {
            String strValue = (String) value;
            // "" (빈 문자열)도 포함하여 처리
            return strValue != null && !strValue.trim().isEmpty();
        } else if (value instanceof Integer || value instanceof Long) {
            return value != null && ((Number) value).longValue() != 0;
        }
        return false;
    }

    // --------------------------------------------------------------
//동과 태그를 입력받아 검색하고 search 테이블에 검색한 태그를 저장
    public List<GetAcademyRes> getAcademyRes(GetAcademyReq p) {
        PostAcademySearch search = new PostAcademySearch();
        search.setTagId(p.getTagId());
        int post = academyMapper.postSearch(search);
        List<GetAcademyRes> res = academyMapper.getAcademy(p);
        if (res.size() == 0) {
            academyMessage.setMessage("학원 검색을 실패했습니다.");
            return null;
        }
        academyMessage.setMessage("학원 검색을 성공했습니다.");
        return res;
    }

    //학원 PK를 받아 학원 상세 정보 불러오기
    public GetAcademyDetail getAcademyDetail(Long acaId) {
        GetAcademyDetail res = academyMapper.getAcademyDetail(acaId);

        if (res == null) {
            academyMessage.setMessage("학원의 상세 정보 불러오기를 실패했습니다.");
            return null;
        }
        academyMessage.setMessage("학원의 상세 정보 불러오기를 성공했습니다.");
        return res;
    }

    //태그 리스트 가져오기
    public List<GetAcademyTagDto> getTagList(Long acaId) {
        return academyMapper.getTagList(acaId);
    }

    //검색어를 입력받아 태그 리스트 불러오기
    public List<GetTagListBySearchNameRes> getTagListBySearchName(GetTagListBySearchNameReq p) {
        List<GetTagListBySearchNameRes> list = academyMapper.getTagListBySearchName(p);
        if (p.getTagName() == null) {
            List<GetTagListBySearchNameRes> allTagList = academyMapper.getAllTagList();
            return allTagList;
        }
        if (list.size() == 0) {
            academyMessage.setMessage("태그 리스트 불러오기 실패");
            return null;
        }
        academyMessage.setMessage("태그 리스트 불러오기 성공!");
        return list;
    }

    //로그인한 유저의 PK를 받아 그 유저가 등록한 학원 리스트 불러오기
    public List<GetAcademyListByUserIdRes> getAcademyListByUserId(GetAcademyListByUserIdReq p) {
        List<GetAcademyListByUserIdRes> list = academyMapper.getAcademyListByUserId(p);
        if (list.size() == 0) {
            academyMessage.setMessage("학원 리스트 불러오기 실패");
            return null;
        }
        academyMessage.setMessage("학원 리스트 불러오기 성공");
        return list;
    }

    //모든 입력을 받아 학원 리스트 출력하기
    public List<GetAcademyListRes> getAcademyListByAll(GetAcademyListReq p) {
        if(p.getTagName() != null){
            Tag tagId = academyMapper.getTagListByTagName(p.getTagName());
            Search search = new Search();
            search.setTag(tagId);
            searchRepository.save(search);
        }
        //int post = academyMapper.postToSearch(p.getTagId());
        List<GetAcademyListRes> list = academyMapper.getAcademyListByAll(p);

        if (list.size() == 0) {
            academyMessage.setMessage("학원 리스트 불러오기 실패");
            return null;
        }
        academyMessage.setMessage("학원 리스트 불러오기 성공");
        return list;
    }

    //학원 상세 모든 정보 보기
    public GetAcademyDetailRes getAcademyDetail(GetAcademyDetailReq p) {
        GetAcademyDetailRes res = academyMapper.getAcademyWithClasses(p);

        if (res == null) {
            academyMessage.setMessage("상세 정보 불러오기 실패");
            return null;
        }

        if (res.getClasses() == null || res.getClasses().isEmpty()) {
            res.setClasses(res.getClasses()); // classes 필드를 아예 제거 (필요 시 JSON 직렬화 시 무시 가능)
        }

        academyMessage.setMessage("상세 정보 불러오기 성공");
        return res;


    }


    public List<GetAcademyRandomRes> generateRandomAcademyList() {
        List<GetAcademyRandomRes> list = academyMapper.getAcademyListRandom();
        if (list.size() == 0) {
            academyMessage.setMessage("학원 출력 실패");
            return null;
        }
        academyMessage.setMessage("학원 출력 성공");
        return list;
    }

    public List<GetAcademyListByStudentRes> getAcademyListByStudent(GetAcademyListByStudentReq p) {
        List<GetAcademyListByStudentRes> list = academyMapper.getAcademyListByStudent(p);
        if (list.size() == 0) {
            academyMessage.setMessage("학원 출력 실패");
            return null;
        }
        academyMessage.setMessage("학원 출력 성공");
        return list;
    }

    public List<PopularSearchRes> popularSearch() {
        List<PopularSearchRes> list = academyMapper.popularSearch();
        if (list.size() == 0) {
            academyMessage.setMessage("인기 검색어가 없습니다.");
            return null;
        }
        academyMessage.setMessage("인기 검색어 출력 완료");
        return list;
    }

    public List<GetDefaultRes> getDefault(Integer size) {
        List<GetDefaultRes> list = academyMapper.getDefault(size);

        if (list.size() == 0) {
            academyMessage.setMessage("디폴트 학원 리스트 출력 실패");
            return null;
        }
        academyMessage.setMessage("디폴트 학원 리스트 출력 성공");
        return list;
    }

    public GetAcademyCountRes GetAcademyCount() {
        return academyMapper.GetAcademyCount();
    }

    public List<GetAcademyInfoRes> getAcademyInfoByAcaNameClassNameExamNameAcaAgree(GetAcademyInfoReq req){
        List<GetAcademyInfoRes> res = academyMapper.getAcademyInfoByAcaNameClassNameExamNameAcaAgree(req);
        if(res == null || res.size() == 0){
            academyMessage.setMessage("검색 조건에 맞는 학원이 없습니다.");
        }
        academyMessage.setMessage("학원 리스트 출력 완료");
        return res;
    }

    public List<GetAcademyListByDistanceRes> getAcademyListByDistance(GetAcademyListByDistanceReq p) {
        List<GetAcademyListByDistanceRes> res = academyMapper.getAcademyListByDistance(p);
        if (res.size() == 0) {
            academyMessage.setMessage("학원 출력 실패");
            return null;
        }
        academyMessage.setMessage("학원 출력 성공");
        return res;
    }

    public List<GetAcaNameListRes> getAcaNameListRes(String acaName){
        return academyMapper.getAcaNameList(acaName);
    }

    public List<GetAcademyListByAcaNameOrderTypeRes> getAcademyListByAcaNameOrderType(GetAcademyListByAcaNameOrderTypeReq p) {
        return academyMapper.getAcademyListByAcaNameOrderType(p);
    }

    public List<GetSearchInfoRes> getSearchInfo(String week){
        return academyMapper.getSearchInfo(week);
    }
}

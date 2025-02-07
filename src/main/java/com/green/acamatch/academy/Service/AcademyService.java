package com.green.acamatch.academy.Service;

import com.green.acamatch.academy.mapper.AcademyMapper;
import com.green.acamatch.academy.model.*;
import com.green.acamatch.academy.model.HB.*;
import com.green.acamatch.academy.model.JW.*;
import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.config.constant.AddressConst;
import com.green.acamatch.config.exception.AcademyException;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.UserMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AcademyService {
    private final AcademyMapper academyMapper;
    private final MyFileUtils myFileUtils;
    private final AcademyMessage academyMessage;
    private final TagService tagService;
    private final AddressConst addressConst;
    private final KakaoApiExample kakaoApiExample;


    //학원정보등록
    @Transactional
    public int insAcademy(MultipartFile pic, AcademyPostReq req) {
        if (req.getTagIdList().isEmpty()) {
            throw new CustomException(AcademyException.MISSING_REQUIRED_FILED_EXCEPTION);
        }

        req.setAddress(addressEncoding(req.getAddressDto()));

        String savedPicName = (pic != null ? myFileUtils.makeRandomFileName(pic) : null);

        req.setAcaPic(savedPicName);

        //req.getAddressDto().getAddress();

        //기본주소를 통해 지번(동)이름 가져오는 api 메소드 호출
        KakaoMapAddress kakaoMapAddressImp = kakaoApiExample.addressSearchMain(req.getAddressDto());

        // 가져온 지번(시) 이름과 매칭되는 시 pk 번호를 select
        Long cityPk = academyMapper.selAddressCity(kakaoMapAddressImp);
        kakaoMapAddressImp.setCityId(cityPk);
        // 가져온 지번(구) 이름과 매칭되는 구 pk 번호를 select
        Long streetPk = academyMapper.selAddressStreet(kakaoMapAddressImp);
        kakaoMapAddressImp.setStreetId(streetPk);
        // 가져온 지번(동) 이름과 매칭되는 동 pk 번호를 select
        Long dongPk = academyMapper.selAddressDong(kakaoMapAddressImp);

        req.setDongId(dongPk);


        academyMapper.insAcademy(req);

        if (pic == null) {
            academyMessage.setMessage("학원정보등록이 완료되었습니다.");
            return 1;
        }

        long acaId = req.getAcaId();
        String middlePath = String.format("academy/%d", acaId);
        myFileUtils.makeFolders(middlePath);
        String filePath = String.format("%s/%s", middlePath, savedPicName);

        try {
            myFileUtils.transferTo(pic, filePath);
        } catch (IOException e) {
            throw new CustomException(AcademyException.PHOTO_SAVE_FAILED);
        }
        tagService.insAcaTag(req);
        academyMessage.setMessage("학원정보등록이 완료되었습니다.");
        return 1;
    }

    //학원정보수정
    @Transactional
    public int updAcademy(MultipartFile pic, AcademyUpdateReq req) {
        //아무것도 입력안했을 때
        if ((pic == null || pic.toString().trim().isEmpty()) &&
                (req.getAcaName() == null || req.getAcaName().trim().isEmpty()) &&
                (req.getAcaPhone() == null || req.getAcaPhone().trim().isEmpty()) &&
                (req.getComment() == null || req.getComment().trim().isEmpty()) &&
                (req.getTeacherNum() == 0) && // int 타입은 null 체크 불필요
                (req.getOpenTime() == null || req.getOpenTime().trim().isEmpty()) &&
                (req.getCloseTime() == null || req.getCloseTime().trim().isEmpty()) &&
                (req.getAddressDto() == null || req.getAddressDto().toString().trim().isEmpty()) &&
                (req.getTagIdList() == null || req.getTagIdList().isEmpty())) {
            throw new CustomException(AcademyException.MISSING_UPDATE_FILED_EXCEPTION);
        }

        // 프로필 사진 처리
        if (pic != null && !pic.isEmpty()) {
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
                myFileUtils.transferTo(pic, filePath);
            } catch (IOException e) {
                throw new CustomException(AcademyException.PHOTO_SAVE_FAILED);
            }
        }

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

        if (req.getAddressDto() != null) {
            //주소수정을 하려고 할때(셋다 값이 들어있을때)
            if (isValidValue(req.getAddressDto().getAddress())
                    && isValidValue(req.getAddressDto().getDetailAddress())
                    && isValidValue(req.getAddressDto().getPostNum())) {
                req.setAddress(addressEncoding(req.getAddressDto()));


                //기본주소를 통해 지번(동)이름 가져오는 api 메소드 호출
                KakaoMapAddress kakaoMapAddressImp = kakaoApiExample.addressSearchMain(req.getAddressDto());

                // 가져온 지번(시) 이름과 매칭되는 시 pk 번호를 select
                Long cityPk = academyMapper.selAddressCity(kakaoMapAddressImp);
                kakaoMapAddressImp.setCityId(cityPk);
                // 가져온 지번(구) 이름과 매칭되는 구 pk 번호를 select
                Long streetPk = academyMapper.selAddressStreet(kakaoMapAddressImp);
                kakaoMapAddressImp.setStreetId(streetPk);
                // 가져온 지번(동) 이름과 매칭되는 동 pk 번호를 select
                Long dongPk = academyMapper.selAddressDong(kakaoMapAddressImp);

                req.setDongId(dongPk);

            }

            String address = req.getAddressDto().getAddress();
            String detailAddress = req.getAddressDto().getDetailAddress();
            String postNum = req.getAddressDto().getPostNum();

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
        if ((req.getTagIdList() != null && !req.getTagIdList().isEmpty()) &&
                (req.getAcaName() == null || req.getAcaName().isEmpty()) &&
                (req.getAcaPhone() == null || req.getAcaPhone().isEmpty()) &&
                (req.getComment() == null || req.getComment().isEmpty()) &&
                req.getTeacherNum() == 0 &&
                (req.getOpenTime() == null || req.getOpenTime().isEmpty()) &&
                (req.getCloseTime() == null || req.getCloseTime().isEmpty()) &&
                (req.getAddress() == null || req.getAddress().isEmpty()) &&
                req.getAddressDto() == null &&
                (req.getAcaPic() == null || req.getAcaPic().isEmpty())) {

            try {
                academyMapper.delAcaTag(req.getAcaId());
                academyMapper.insAcaTag(req.getAcaId(), req.getTagIdList());

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
        return result;
    }

    //학원정보삭제
    public int delAcademy(AcademyDeleteReq req) {
        academyMapper.delAcaTag(req.getAcaId());
        int result = academyMapper.delAcademy(req.getAcaId(), req.getUserId());

        if (result == 1) {
            academyMessage.setMessage("학원정보가 삭제되었습니다.");
            return result;
        } else {
            academyMessage.setMessage("학원정보 삭제가 실패하였습니다.");
            return result;
        }
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
        for (GetAcademyRes re : res) {
            re.setAddressDto(addressDecoding(re.getAddress()));
            re.setAddress(re.getAddressDto().getAddress());
        }
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
        res.setAddressDto(addressDecoding(res.getAddress()));
        res.setAddress(res.getAddressDto().getAddress());
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
        int post = academyMapper.postToSearch(p.getTagId());
        List<GetAcademyListRes> list = academyMapper.getAcademyListByAll(p);
        for (GetAcademyListRes re : list) {
            re.setAddressDto(addressDecoding(re.getAddress()));
            re.setAddress(re.getAddressDto().getAddress());
        }
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

        res.setAddressDto(addressDecoding(res.getAddress()));
        res.setAddress(res.getAddressDto().getAddress());

        if (res.getClasses() == null || res.getClasses().isEmpty()) {
            res.setClasses(null); // classes 필드를 아예 제거 (필요 시 JSON 직렬화 시 무시 가능)
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

    public List<GetDefaultRes> getDefault() {
        List<GetDefaultRes> list = academyMapper.getDefault();
        for (GetDefaultRes re : list) {
            re.setAddressDto(addressDecoding(re.getAddress()));
            re.setAddress(re.getAddressDto().getAddress());
        }
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
}

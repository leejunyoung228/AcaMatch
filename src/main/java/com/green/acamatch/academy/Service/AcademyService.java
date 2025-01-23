package com.green.acamatch.academy.Service;

import com.green.acamatch.academy.mapper.AcademyMapper;
import com.green.acamatch.academy.model.*;
import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.config.constant.AddressConst;
import com.green.acamatch.config.exception.UserMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AcademyService {
    private final AcademyMapper academyMapper;
    private final MyFileUtils myFileUtils;
    private final AcademyMessage academyMessage;
    private final UserMessage userMessage;
    private final AddressConst addressConst;


    //학원정보등록
    @Transactional
    public int insAcademy(MultipartFile pic, AcademyPostReq req) {

        req.setAddress(addressEncoding(req.getAddressDto()));

        String savedPicName = (pic != null ? myFileUtils.makeRandomFileName(pic) : null);

        req.setAcaPic(savedPicName);

        int result = academyMapper.insAcademy(req);

        if(result ==0) {
            academyMessage.setMessage("학원정보등록이 실패하였습니다.");
            return result;
        }

        if(pic == null){
            academyMessage.setMessage("학원정보등록이 완료되었습니다.");
            return result;
        }

        long acaId = req.getAcaId();
        String middlePath = String.format("academy/%d", acaId);
        myFileUtils.makeFolders(middlePath);
        String filePath = String.format("%s/%s", middlePath, savedPicName);

        try{
            myFileUtils.transferTo(pic, filePath);
        }catch (IOException e){
            e.printStackTrace();
        }

        academyMessage.setMessage("학원정보등록이 완료되었습니다.");
        return 1;

    }


    //학원정보수정
    @Transactional
    public int updAcademy(MultipartFile pic, AcademyUpdateReq req) {
        //아무것도 입력안했을 때


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

            try{
                myFileUtils.transferTo(pic, filePath);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        int result = academyMapper.updAcademy(req);


        if (result == 0) {
            academyMessage.setMessage("학원정보수정을 실패하였습니다.");
            return result;
        }

        if(req.getTagIdList() != null) {
            academyMapper.delAcaTag(req.getAcaId());
            int result2 = academyMapper.insAcaTag(req.getAcaId(), req.getTagIdList());
            if(result2 == 0){
                academyMessage.setMessage("태그문제로 정보수정이 실패하였습니다.");
                return result2;
            }
        }
        academyMessage.setMessage("학원정보수정이 완료되었습니다.");
        return result;
    }

    //학원정보삭제
    public int delAcademy(AcademyDeleteReq req) {
        academyMapper.delAcaTag(req.getAcaId());
        int result = academyMapper.delAcademy(req.getAcaId(), req.getUserId());

        if(result == 1) {
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

        if(list == null) {
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
                        address.indexOf(addressConst.getEndSep()+addressConst.getPostNumSep())
                ));
        return res;
    }


// --------------------------------------------------------------
    //동과 태그를 입력받아 검색하고 search 테이블에 검색한 태그를 저장
    public List<GetAcademyRes> getAcademyRes(GetAcademyReq p){
        PostAcademySearch search = new PostAcademySearch();
        search.setTagId(p.getTagId());
        int post = academyMapper.postSearch(search);
        List<GetAcademyRes> res = academyMapper.getAcademy(p);
        if(res.size() == 0) {
            academyMessage.setMessage("학원 검색을 실패했습니다.");
            return null;
        }
        academyMessage.setMessage("학원 검색을 성공했습니다.");
        return res;
    }

    //학원 PK를 받아 학원 상세 정보 불러오기
    public GetAcademyDetail getAcademyDetail(Long acaId){
        GetAcademyDetail res = academyMapper.getAcademyDetail(acaId);
        if(res == null) {
            academyMessage.setMessage("학원의 상세 정보 불러오기를 실패했습니다.");
            return null;
        }
        academyMessage.setMessage("학원의 상세 정보 불러오기를 성공했습니다.");
        return res;
    }

    //태그 리스트 가져오기
    public List<GetAcademyTagDto> getTagList(Long acaId){
        return academyMapper.getTagList(acaId);
    }

    // 도시 리스트 가져오기
    public List<GetCityRes> getCityList() {return academyMapper.getCity();}

    // 시/군/구 리스트 가져오기
    public List<GetStreetRes> getStreetList(GetStreetReq p) {
        List<GetStreetRes> list = academyMapper.getStreet(p);
        if(list == null){
            academyMessage.setMessage("시/군/구 리스트를 불러오기 실패");
            return null;
        }
        academyMessage.setMessage("시/군/구 리스트를 불러오기 성공");
        return academyMapper.getStreet(p);
    }

    // 동 리스트 가져오기
    public List<GetDongRes> getDongList(GetDongReq p) {
        List<GetDongRes> list = academyMapper.getDong(p);
        if(list == null){
            academyMessage.setMessage("동 리스트 불러오기 실패");
            return null;
        }
        academyMessage.setMessage("동 리스트 불러오기 성공");
        return list;
    }

    //동만 입력받아 학원 리스트 불러오기
    public List<GetAcademyByDongRes> getAcademyByDongResList(GetAcademyByDongReq p){
        List<GetAcademyByDongRes> list = academyMapper.getAcademyListByDong(p);
        if(list == null){
            academyMessage.setMessage("동만 입력받아 학원 리스트 불러오기 실패");
            return null;
        }
        academyMessage.setMessage("동만 입력받아 학원 리스트 불러오기 성공");
        return list;
    }
}

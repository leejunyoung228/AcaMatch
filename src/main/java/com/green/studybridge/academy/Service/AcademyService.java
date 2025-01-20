package com.green.studybridge.academy.Service;

import com.green.studybridge.academy.mapper.AcademyMapper;
import com.green.studybridge.academy.mapper.TagMapper;
import com.green.studybridge.academy.model.*;
import com.green.studybridge.academy.model.category.CategoryGetAgeRangeRes;
import com.green.studybridge.academy.model.category.CategoryGetDaysRes;
import com.green.studybridge.academy.model.category.CategoryGetLevelRes;
import com.green.studybridge.academy.model.tag.InsTagWithAcademy;
import com.green.studybridge.academy.model.tag.SelTagDto;
import com.green.studybridge.academy.model.tag.SelTagRes;
import com.green.studybridge.config.MyFileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AcademyService {
    private final AcademyMapper academyMapper;
    private final MyFileUtils myFileUtils;
    private final UserMessage userMessage;
    private final TagMapper tagMapper;
    private InsTagWithAcademy insTagWithAcademy;



    //카테고리는 강좌에서 다루는거라 일단 뱄음
    /*//카테고리 select
    public List<CategoryGetAgeRangeRes> categoryAgeRangeResList() {
        List<CategoryGetAgeRangeRes> list = academyMapper.selAgeRangeList();
        return list;
    }

    public List<CategoryGetLevelRes> categoryLevelResList() {
        List<CategoryGetLevelRes> list = academyMapper.selLevelList();
        return list;
    }

    public List<CategoryGetDaysRes> categoryDaysResList() {
        List<CategoryGetDaysRes> list = academyMapper.selDaysList();
        return list;
    }

    //학원이 등록한 카테고리 insert
    public int insAcaAgeRange(AcademyPostReq req) {
        int result = academyMapper.insAcaAgeRange(req);
        return result;
    }

    public int insAcaLevel(AcademyPostReq req) {
        int result = academyMapper.insAcaLevel(req);
        return result;
    }

    public int insAcaDays(AcademyPostReq req) {
        int result = academyMapper.insAcaDays(req);
        return result;
    }*/


    //학원정보등록
    public int insAcademy(MultipartFile pic, AcademyPostReq req) {

        String savedPicName = (pic != null ? myFileUtils.makeRandomFileName(pic) : null);

        req.setAcaPic(savedPicName);

        int result = academyMapper.insAcademy(req);
        insTagWithAcademy.setAcaId(req.getAcaId());

        if(pic == null){
            userMessage.setMessage("학원정보등록이 완료되었습니다.");
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
        return result;
    }


    //학원정보수정
    public int updAcademy(MultipartFile pic, AcademyUpdateReq req) {
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

        academyMapper.updAcademy(req);
        tagMapper.delAcaTag(req);
        tagMapper.insAcaTag(req.getAcaId(), req.getTagIdList());
        return 1;
    }

    //학원정보삭제
    public int delAcademy(AcademyDeleteReq req) {
        academyMapper.delAcademy(req);
        return 1;
    }

// --------------------------------------------------------------

    public List<getAcademyRes> getAcademyRes(getAcademyReq p){
        List<getAcademyRes> res = academyMapper.getAcademy(p);
        return res;
    }

    public GetAcademyDetail getAcademyDetail(Long acaId){
        GetAcademyDetail res = academyMapper.getAcademyDetail(acaId);
        return res;
    }


}

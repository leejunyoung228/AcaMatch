package com.green.studybridge.academy.Service;

import com.green.studybridge.academy.mapper.AcademyMapper;
import com.green.studybridge.academy.model.*;
import com.green.studybridge.academy.tag.InsTagWithAcademy;
import com.green.studybridge.academy.tag.SelTagDto;
import com.green.studybridge.academy.tag.SelTagReq;
import com.green.studybridge.academy.tag.SelTagRes;
import com.green.studybridge.config.MyFileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagService {
    private final AcademyMapper academyMapper;
    private final MyFileUtils myFileUtils;
    private InsTagWithAcademy insTagWithAcademy;


    //모든태그 불러오기
    public SelTagRes selTagList(SelTagReq req) {
        List<SelTagDto> list = academyMapper.selTagDtoList(req);

        SelTagRes res = new SelTagRes();
        res.setSelTagList(list);
        return res;
    }

    /*//한별 태그(수정필요해서 주석처리함)
    public List<GetTagList> getTagList(){
        return academyMapper.getTagList();
    }*/


    //학원등록할때 태그 insert
    public int insAcaTag(AcademyPostReq req) {
        int result = academyMapper.insAcaTag(req.getAcaId(), req.getTagIdList());
        return result;
    }

    //학원태그 수정을 위한 delete
    public int delAcaTag(AcademyUpdateReq req) {
        int result = academyMapper.delAcaTag(req);
        return result;
    }


}

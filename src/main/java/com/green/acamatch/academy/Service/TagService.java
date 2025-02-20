package com.green.acamatch.academy.Service;

import com.green.acamatch.academy.AcademyTagRepository;
import com.green.acamatch.academy.mapper.AcademyMapper;
import com.green.acamatch.academy.model.JW.AcademyMessage;
import com.green.acamatch.academy.model.JW.AcademyPostReq;
import com.green.acamatch.academy.model.JW.AcademyUpdateReq;
import com.green.acamatch.academy.tag.SelTagDto;
import com.green.acamatch.academy.tag.SelTagReq;
import com.green.acamatch.academy.tag.SelTagRes;
import com.green.acamatch.config.exception.AcademyException;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.entity.academy.AcademyTag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagService {
    private final AcademyMapper academyMapper;
    private final AcademyMessage academyMessage;
    private final AcademyTagRepository academyTagRepository;


    //모든태그 불러오기
    public SelTagRes selTagList(SelTagReq req) {
        List<SelTagDto> list = academyMapper.selTagDtoList(req);
        if(list == null || list.isEmpty()) {
            academyMessage.setMessage(req.getSearchTag() + " 관련된 태그가 없습니다.");
            SelTagRes res = new SelTagRes();
            res.setSelTagList(list);
            return res;
        }

        academyMessage.setMessage(req.getSearchTag() + " 관련된 태그입니다.");
        SelTagRes res = new SelTagRes();
        res.setSelTagList(list);
        return res;
    }

    //학원등록할때 태그 insert
    public int insAcaTag(AcademyPostReq req) {
        return academyMapper.insAcaTag(req.getAcaId(), req.getTagIdList());
    }


    //학원태그 수정을 위한 delete
    public int delAcaTag(AcademyUpdateReq req) {
        int result = academyMapper.delAcaTag(req.getAcaId());
        return result;
    }


}

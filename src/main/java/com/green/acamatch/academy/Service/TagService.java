package com.green.acamatch.academy.Service;


import com.green.acamatch.academy.mapper.AcademyMapper;
import com.green.acamatch.academy.model.JW.AcademyMessage;
import com.green.acamatch.academy.model.JW.AcademyPostReq;
import com.green.acamatch.academy.model.JW.AcademyUpdateReq;
import com.green.acamatch.academy.tag.*;

import com.green.acamatch.academy.tag.TagRepository;
import com.green.acamatch.entity.tag.AcademyTag;
import com.green.acamatch.entity.tag.AcademyTagIds;
import com.green.acamatch.entity.tag.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagService {
    private final AcademyMapper academyMapper;
    private final AcademyMessage academyMessage;

    private final TagRepository tagRepository;
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
    @Transactional
    public void insTag(List<String> req) {
        List<String> tagNames = req; // 입력받은 태그 리스트

        // DB에 이미 존재하는 태그 목록 조회
        List<Tag> existingTags = tagRepository.findByTagNameIn(tagNames);
        Set<String> existingTagNames = existingTags.stream()
                .map(Tag::getTagName)
                .collect(Collectors.toSet());

        // 존재하지 않는 태그만 필터링하여 저장
        List<Tag> newTags = tagNames.stream()
                .filter(tagName -> !existingTagNames.contains(tagName)) // 기존 태그 제외
                .map(tagName -> new Tag()) // 새 Tag 객체 생성
                .collect(Collectors.toList());

        tagRepository.saveAll(newTags); // 한 번에 저장
    }

    //학원등록할때 학원태그 insert
    @Transactional
    public void insAcaTag(Long acaId, List<Long> tagIdList) {
        for(Long tagId : tagIdList) {
            AcademyTagIds academyTagIds = new AcademyTagIds();
            academyTagIds.setAcaId(acaId);
            academyTagIds.setTagId(tagId);

            AcademyTag academyTag = new AcademyTag();
            academyTag.setAcademyTagIds(academyTagIds);

            academyTagRepository.save(academyTag);
        }

    }


    //학원태그 수정을 위한 delete
    public void delAcaTag(Long acaId) {
        /*int result = academyMapper.delAcaTag(req.getAcaId());
        return result;*/ //2차때 사용함

        int affecteRows = academyTagRepository.deleteAcademytag(acaId);
        log.info("affecteRows: {}", affecteRows);
    }


}

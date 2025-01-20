package com.green.studybridge.academy.mapper;

import com.green.studybridge.academy.model.AcademyPostReq;
import com.green.studybridge.academy.model.AcademyTagDeleteReq;
import com.green.studybridge.academy.model.AcademyUpdateReq;
import com.green.studybridge.academy.model.tag.SelTagDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TagMapper {
    List<SelTagDto> selTagDtoList();
    int insAcaTag(long acaId, List<Long>TagIdList);
    int delAcaTag(AcademyUpdateReq req);
}

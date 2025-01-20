package com.green.studybridge.subject;

import com.green.studybridge.subject.model.SubjectGetDto;
import com.green.studybridge.subject.model.SubjectGetReq;
import com.green.studybridge.subject.model.SubjectPostReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SubjectMapper {
    int insSubjectScore(SubjectPostReq p);
    List<SubjectGetDto> selSubjectScore(SubjectGetReq p);
}
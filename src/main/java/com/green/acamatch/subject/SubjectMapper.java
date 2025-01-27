package com.green.acamatch.subject;

import com.green.acamatch.subject.model.SubjectGetDto;
import com.green.acamatch.subject.model.SubjectGetReq;
import com.green.acamatch.subject.model.SubjectPostReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SubjectMapper {
    int insSubjectScore(SubjectPostReq p);
    int existsSubject(long classId, String subjectName);
    List<SubjectGetDto> selSubject(SubjectGetReq p);
}
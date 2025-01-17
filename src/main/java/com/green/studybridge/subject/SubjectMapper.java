package com.green.studybridge.subject;

import com.green.studybridge.subject.model.SubjectPostReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SubjectMapper {
    int insSubjectScore(SubjectPostReq p);
}
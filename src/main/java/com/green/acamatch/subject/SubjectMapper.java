package com.green.acamatch.subject;

import com.green.acamatch.subject.model.SubjectPostReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SubjectMapper {
    int insSubjectScore(SubjectPostReq p);
}
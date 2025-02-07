package com.green.acamatch.subject;

import com.green.acamatch.subject.model.SubjectPostReq;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.validation.annotation.Validated;

@Mapper
@Validated
public interface SubjectMapper {
    int insSubjectScore(SubjectPostReq p);
    int existsSubject(long classId, String subjectName);
}
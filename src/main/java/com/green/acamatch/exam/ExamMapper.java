package com.green.acamatch.exam;

import com.green.acamatch.exam.model.ExamPostReq;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.validation.annotation.Validated;

@Mapper
@Validated
public interface ExamMapper {
    int insExamScore(ExamPostReq p);
    int existsExam(long classId, String examName);
}
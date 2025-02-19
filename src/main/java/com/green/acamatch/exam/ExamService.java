package com.green.acamatch.exam;

import com.green.acamatch.exam.model.ExamPostReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamService {
    private final ExamMapper mapper;

    public int postExam(ExamPostReq p) {
        int exists = mapper.existsExam(p.getClassId(), p.getExamName());
        if (exists > 0) {
            throw new IllegalArgumentException("이미 존재하는 시험입니다.");
        }
        int result = mapper.insExamScore(p);
        return result;
    }
}
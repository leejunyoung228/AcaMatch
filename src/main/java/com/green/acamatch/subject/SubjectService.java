package com.green.acamatch.subject;

import com.green.acamatch.subject.model.SubjectPostReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectMapper mapper;

    public int postSubject(SubjectPostReq p) {
        int exists = mapper.existsSubject(p.getClassId(), p.getSubjectName());
        if (exists > 0) {
            throw new IllegalArgumentException("이미 존재하는 시험입니다.");
        }
        int result = mapper.insSubjectScore(p);
        return result;
    }
}
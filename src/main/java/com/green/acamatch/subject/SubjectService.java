package com.green.acamatch.subject;

import com.green.acamatch.subject.model.SubjectPostReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectMapper mapper;

    public int postSubject(SubjectPostReq p) {
        return mapper.insSubjectScore(p);
    }
}

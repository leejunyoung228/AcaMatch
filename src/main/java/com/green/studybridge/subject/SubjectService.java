package com.green.studybridge.subject;

import com.green.studybridge.subject.model.SubjectPostReq;
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

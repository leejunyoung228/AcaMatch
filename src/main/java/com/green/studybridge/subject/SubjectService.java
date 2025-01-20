package com.green.studybridge.subject;

import com.green.studybridge.subject.model.SubjectGetDto;
import com.green.studybridge.subject.model.SubjectGetReq;
import com.green.studybridge.subject.model.SubjectPostReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectMapper mapper;

    public int postSubject(SubjectPostReq p) {
        return mapper.insSubjectScore(p);
    }

    public List<SubjectGetDto> selSubjectScore(SubjectGetReq p) {
        return mapper.selSubjectScore(p);
    }
}

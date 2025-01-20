package com.green.studybridge.grade;

import com.green.studybridge.grade.model.GradePostReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GradeService {
    private final GradeMapper mapper;

    public int postGrade(GradePostReq p) {
        return mapper.insGrade(p);
    }
}

package com.green.studybridge.grade;

import com.green.studybridge.grade.model.GradePostReq;
import com.green.studybridge.grade.model.GradeGetDto;
import com.green.studybridge.grade.model.GradeGetReq;
import com.green.studybridge.grade.model.GradePutReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {
    private final GradeMapper mapper;

    public int postGrade(GradePostReq p) {
        return mapper.insGrade(p);
    }

    public List<GradeGetDto> selGradeScore(GradeGetReq p) {
        return mapper.selGradeScore(p);
    }

    public int updGradeScore(GradePutReq p) {
        return mapper.updGradeScore(p);
    }
}

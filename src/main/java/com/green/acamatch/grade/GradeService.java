package com.green.acamatch.grade;

import com.green.acamatch.grade.model.GradePostReq;
import com.green.acamatch.grade.model.GradeGetDto;
import com.green.acamatch.grade.model.GradeGetReq;
import com.green.acamatch.grade.model.GradePutReq;
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

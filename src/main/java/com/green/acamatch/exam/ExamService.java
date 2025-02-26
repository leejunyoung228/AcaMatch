package com.green.acamatch.exam;

import com.green.acamatch.acaClass.ClassRepository;
import com.green.acamatch.config.exception.AcaClassErrorCode;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.entity.acaClass.AcaClass;
import com.green.acamatch.entity.exam.Exam;
import com.green.acamatch.exam.model.ExamPostReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamService {
    private final ExamMapper mapper;
    private final ClassRepository classRepository;
    private final ExamRepository examRepository;

    public int postExam(ExamPostReq p) {
        try {
            if (examRepository.existsExam(p.getExamId(), p.getExamName()) > 0) {
                throw new IllegalArgumentException("이미 존재하는 시험입니다.");
            }

            Exam exam = new Exam();
            AcaClass acaClass = new AcaClass();
            classRepository.findById(p.getClassId()).orElseThrow(() -> new CustomException(AcaClassErrorCode.NOT_FOUND_CLASS));
            exam.setClassId(acaClass);

            exam.setExamName(p.getExamName());
            exam.setExamType(p.getExamType());
            examRepository.save(exam);

            return 1;
        } catch (CustomException e) {
            e.getMessage();
            return 0;
        }
    }
}
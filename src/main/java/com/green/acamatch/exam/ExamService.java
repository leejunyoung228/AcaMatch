package com.green.acamatch.exam;

import com.green.acamatch.acaClass.ClassRepository;
import com.green.acamatch.academy.AcademyRepository;
import com.green.acamatch.config.exception.AcaClassErrorCode;
import com.green.acamatch.config.exception.AcademyException;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.entity.acaClass.AcaClass;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.exam.Exam;
import com.green.acamatch.exam.model.ExamPostReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExamService {
    private final ExamMapper mapper;
    private final ClassRepository classRepository;
    private final ExamRepository examRepository;
    private final AcademyRepository academyRepository;

    @Transactional
    public int postExam(ExamPostReq p) {
        try {
            // 1. 중복 검사
            if (examRepository.existsExam(p.getExamId(), p.getExamName()) > 0) {
                throw new IllegalArgumentException("이미 존재하는 시험입니다.");
            }

            // 2. 반 조회 (존재하지 않으면 예외 발생)
            AcaClass acaClass = classRepository.findById(p.getClassId())
                    .orElseThrow(() -> new CustomException(AcaClassErrorCode.NOT_FOUND_CLASS));

            // 3. 시험 객체 생성 및 설정
            Exam exam = new Exam();
            exam.setExamName(p.getExamName());
            exam.setExamType(p.getExamType());
            exam.setClassId(acaClass); // `AcaClass` 설정

            // 4. 시험 저장
            examRepository.save(exam);

            return 1;
        } catch (CustomException e) {
            e.getMessage();
            return 0;
        }
    }
}
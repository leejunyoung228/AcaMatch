package com.green.acamatch.grade;

import com.green.acamatch.config.exception.AcaClassErrorCode;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.entity.exam.Exam;
import com.green.acamatch.entity.grade.Grade;
import com.green.acamatch.entity.joinClass.JoinClass;
import com.green.acamatch.exam.ExamRepository;
import com.green.acamatch.grade.model.*;
import com.green.acamatch.joinClass.JoinClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {
    private final GradeMapper mapper;
    private final UserMessage userMessage;
    private final JoinClassRepository joinClassRepository;
    private final ExamRepository examRepository;
    private final GradeRepository gradeRepository;

    //성적 입력(joinclass에서 학생이 시험을 쳐야 성적 입력 가능)
    @Transactional
    public int postGrade(GradePostReq p) {
        try {
            if (gradeRepository.existsGrade(p.getJoinClassId(), p.getExamId()) > 0) {
                throw new IllegalArgumentException("이미 성적 등록 하였습니다.");
            }
            Grade grade = new Grade();
            JoinClass joinClass = joinClassRepository.findById(p.getJoinClassId()).orElseThrow(()
                    -> new CustomException(AcaClassErrorCode.NOT_FOUND_JOIN_CLASS));
            Exam exam = examRepository.findById(p.getExamId()).orElseThrow(()
                    -> new CustomException(AcaClassErrorCode.NOT_FOUND_EXAM));

            grade.setJoinClass(joinClass);
            grade.setExam(exam);
            grade.setScore(p.getScore());
            grade.setPass(p.getPass());
            grade.setExamDate(p.getExamDate());
            grade.setProcessingStatus(p.getProcessingStatus());

            gradeRepository.save(grade);
            return 1;
        } catch (CustomException e) {
            e.getStackTrace();
            return 0;
        }
    }

    //성적 불러오기
    public List<GradeGetDto> selGradeScore(GradeGetReq p) {
        try {
            List<GradeGetDto> result = mapper.selGradeScore(p);
            if (result == null || result.isEmpty()) {
                userMessage.setMessage("성적 불러오기에 실패하였습니다.");
                return null;
            }
            userMessage.setMessage("성적 불러오기에 성공하였습니다.");
            return result;
        } catch (Exception e) {
            userMessage.setMessage("기타 오류 사항으로 성적을 불러오지 못했습니다.");
            return null;
        }
    }

    public List<GradeDetailDto> selGradeDetail(GradeDetailGetReq p) {
        try {
            List<GradeDetailDto> result = mapper.selGradeDetail(p);
            if (result == null || result.isEmpty()) {
                throw new CustomException(AcaClassErrorCode.NOT_FOUND_GRADE);
            }
            return result;
        } catch (CustomException e) {
            e.getStackTrace();
            return null;
        }
    }

    public List<GradeStatusGetDto> selGradeStatus(GradeStatusGetReq p) {
        try {
            List<GradeStatusGetDto> result = mapper.selGradeStatus(p);
            if (result == null || result.isEmpty()) {
                userMessage.setMessage("시험 처리 상태 불러오기에 실패하였습니다.");
                return null;
            }
            userMessage.setMessage("시험 처리 상태 불러오기에 성공하였습니다.");
            return result;
        } catch (Exception e) {
            userMessage.setMessage("기타 오류 사항으로 정보를 불러오지 못했습니다.");
            return null;
        }
    }

    public List<GradeUserAndParentGetRes> selGradeUserAndParent(GradeUserAndParentGetReq p) {
        try {
            List<GradeUserAndParentGetRes> result = mapper.selGradeUserAndParent(p);
            if (result == null || result.isEmpty()) {
                throw new CustomException(AcaClassErrorCode.FAIL_TO_SEL);
            }
            return result;
        }catch (CustomException e) {
            e.getStackTrace();
            return null;
        }
    }

    //성적 수정하기
    @Transactional
    public int updGradeScore(GradePutReq p) {
        try {
            Grade grade = gradeRepository.findById(p.getGradeId()).orElseThrow(() -> new CustomException(AcaClassErrorCode.NOT_FOUND_GRADE));
            grade.setGradeId(p.getGradeId());
            if (p.getScore() < 0) {
                throw new CustomException(AcaClassErrorCode.FAIL_TO_UPD);
            }
            if (p.getPass() < 0) {
                throw new CustomException(AcaClassErrorCode.FAIL_TO_UPD);
            }
            if (p.getProcessingStatus() < 0) {
                throw new CustomException(AcaClassErrorCode.FAIL_TO_UPD);
            }

            grade.setExamDate(p.getExamDate());
            grade.setScore(p.getScore());
            grade.setPass(p.getPass());
            grade.setProcessingStatus(p.getProcessingStatus());

            gradeRepository.save(grade);
            return 1;
        } catch (Exception e) {
            e.getStackTrace();
            return 0;
        }
    }
}
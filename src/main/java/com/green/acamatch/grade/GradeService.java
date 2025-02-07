package com.green.acamatch.grade;

import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.grade.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {
    private final GradeMapper mapper;
    private final UserMessage userMessage;

    //성적 입력(joinclass에서 학생이 시험을 쳐야 성적 입력 가능)
    @Transactional
    public int postGrade(GradePostReq p) {
        try {
            int exists = mapper.existsGrade(p.getJoinClassId(), p.getSubjectId());
            if (exists > 0) {
                userMessage.setMessage("이미 성적을 입력하였습니다.");
                return 0;
            }
            int result = mapper.insGrade(p);
            userMessage.setMessage("성적이 성공적으로 입력되었습니다.");
            return result;
        } catch (BadSqlGrammarException e) {
            userMessage.setMessage("잘못된 형식을 입력하였습니다.");
            return 0;
        } catch (Exception e) {
            userMessage.setMessage("잘못된 값을 입력하였습니다.");
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

    public List<GradeUserDto> selGradeUser(GradeUserGetReq p) {
        try {
            List<GradeUserDto> result = mapper.selGradeUser(p);
            if (result == null || result.isEmpty()) {
                userMessage.setMessage("수강생들의 성적이 없습니다.");
                return null;
            }
            userMessage.setMessage("수강생들의 성적 불러오기를 성공하였습니다.");
            return result;
        } catch (Exception e) {
            userMessage.setMessage("수강생들의 성적 불러오기를 실패하였습니다.");
            return null;
        }
    }

    public List<GradeStatusGetDto> selGradeStatus(GradeStatusGetReq p) {
        try {
            List<GradeStatusGetDto> result = mapper.selGradeStatus(p);
            if (result == null || result.isEmpty()) {
                userMessage.setMessage("시험 처리 상태 불러오기에 실패하였습니다.");
                return result;
            }
            userMessage.setMessage("시험 처리 상태 불러오기에 성공하였습니다.");
            return result;
        } catch (Exception e) {
            userMessage.setMessage("기타 오류 사항으로 정보를 불러오지 못했습니다.");
            return null;
        }
    }

    //성적 수정하기
    public int updGradeScore(GradePutReq p) {
            int result = mapper.updGradeScore(p);
            if (result == 0) {
                userMessage.setMessage("수정할 정보가 존재하지 않습니다.");
                return 0;
            }
            userMessage.setMessage("성적 수정에 성공하였습니다.");
            return result;
    }
}
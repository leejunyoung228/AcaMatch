package com.green.acamatch.subject;

import com.green.acamatch.acaClass.model.AcaClassDto;
import com.green.acamatch.acaClass.model.AcaClassGetReq;
import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.subject.model.SubjectGetDto;
import com.green.acamatch.subject.model.SubjectGetReq;
import com.green.acamatch.subject.model.SubjectPostReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectMapper mapper;
    private final UserMessage userMessage;

    public int postSubject(SubjectPostReq p) {
        int exists = mapper.existsSubject(p.getClassId(), p.getSubjectName());
        if (exists > 0) {
            throw new IllegalArgumentException("이미 존재하는 시험입니다.");
        }
        int result = mapper.insSubjectScore(p);
        return result;
    }

    public List<SubjectGetDto> selSubject(SubjectGetReq p) {
            try {
                List<SubjectGetDto> result = mapper.selSubject(p);
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
    }
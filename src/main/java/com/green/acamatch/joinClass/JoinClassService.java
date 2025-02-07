package com.green.acamatch.joinClass;

import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.security.AuthenticationFacade;
import com.green.acamatch.grade.model.GradeUserDto;
import com.green.acamatch.grade.model.GradeUserGetReq;
import com.green.acamatch.joinClass.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JoinClassService {
    private final JoinClassMapper mapper;
    private final UserMessage userMessage;

    //수강생 등록
    @Transactional
    public int postJoinClass(JoinClassPostReq p) {
        int exists = mapper.existsJoinClass(p.getClassId(), p.getUserId());
        if (exists > 0) {
            throw new IllegalArgumentException("이미 수강 신청하였습니다.");
        }
        int result = mapper.insJoinClass(p);
        return result;
    }

    public List<JoinClassDto> selJoinClass(JoinClassGetReq p) {
        p.setUserId(AuthenticationFacade.getSignedUserId());
        try {
            List<JoinClassDto> result = mapper.selJoinClass(p);
            if (result == null || result.isEmpty()) {
                userMessage.setMessage("성적확인을 위한 학원명/강좌명 불러오기에 실패하였습니다.");
                return null;
            }
            userMessage.setMessage("성적확인을 위한 학원명/강좌명 불러오기에 성공하였습니다.");
            return result;
        } catch (Exception e) {
            userMessage.setMessage("기타 오류 사항으로 성적확인을위한 불러오지 못했습니다.");
            return null;
        }
    }

    public int putJoinClass(JoinClassPutReq p) {
        try {
            int result = mapper.updJoinClass(p);

            if (result == 0) {
                userMessage.setMessage("수정에 실패하였습니다.");
                return 0;
            }
            userMessage.setMessage("수정에 성공하였습니다.");
            return result;
        } catch (Exception e) {
            userMessage.setMessage("수정중에 오류가 발생하였습니다.");
            return 0;
        }
    }

    // 수강생 삭제
    public int delJoinClass(JoinClassDel p) {
        int result = mapper.delJoinClass(p);
        if (result == 1) {
            userMessage.setMessage("수강생 삭제에 성공하였습니다.");
            return result;
        } else {
            userMessage.setMessage("수강생 삭제에 실패하였습니다.");
            return 0;
        }
    }
}

package com.green.acamatch.joinClass;

import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.joinClass.model.JoinClassDel;
import com.green.acamatch.joinClass.model.JoinClassPostReq;
import com.green.acamatch.joinClass.model.JoinClassPutReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public int putJoinClass(JoinClassPutReq p) {
       try {
            int result = mapper.updJoinClass(p);

            if(result == 0) {
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

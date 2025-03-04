package com.green.acamatch.joinClass;

import com.green.acamatch.acaClass.ClassRepository;
import com.green.acamatch.config.exception.AcaClassErrorCode;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.UserErrorCode;
import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.security.AuthenticationFacade;
import com.green.acamatch.entity.acaClass.AcaClass;
import com.green.acamatch.entity.joinClass.JoinClass;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.joinClass.model.*;
import com.green.acamatch.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JoinClassService {
    private final JoinClassMapper mapper;
    private final UserMessage userMessage;
    private final ClassRepository classRepository;
    private final UserRepository userRepository;
    private final JoinClassRepository joinClassRepository;

    //수강생 등록
    @Transactional
    public int postJoinClass(JoinClassPostReq p) {
        try {
            if (joinClassRepository.existsJoinClass(p.getClassId(), p.getUserId()) > 0) {
                throw new IllegalArgumentException("이미 수강 신청하였습니다.");
            }
            JoinClass joinClass = new JoinClass();
            AcaClass acaClass = classRepository.findById(p.getClassId()).orElseThrow(() -> new CustomException(AcaClassErrorCode.NOT_FOUND_CLASS));
            User user = userRepository.findById(p.getUserId()).orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
            joinClass.setAcaClass(acaClass);
            joinClass.setUser(user);
            joinClass.setCertification(p.getCertification());
            joinClass.setRegistrationDate(p.getRegistrationDate());
            joinClassRepository.save(joinClass);
            return 1;
        } catch (CustomException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<JoinClassDto> selJoinClass(JoinClassGetReq p) {
        p.setUserId(AuthenticationFacade.getSignedUserId());
        try {
            List<JoinClassDto> result = null;
            if (p.getRole() == 1) result = mapper.selJoinClass(p);
            else if (p.getRole() == 2) result = mapper.selParents(p);
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

    @Transactional
    public int putJoinClass(JoinClassPutReq p) {
        try {
            JoinClass joinClass = joinClassRepository.findById(p.getJoinClassId()).orElseThrow(() -> new CustomException(AcaClassErrorCode.NOT_FOUND_JOIN_CLASS));
            joinClass.setJoinClassId(p.getJoinClassId());

            if (p.getCertification() > 0) {
                throw new CustomException(AcaClassErrorCode.FAIL_TO_UPD);
            }

            joinClass.setCertification(p.getCertification());
            joinClassRepository.save(joinClass);

            return 1;
        } catch (CustomException e) {
            e.getMessage();
            return 0;
        }
    }

    // 수강생 삭제
    public int delJoinClass(JoinClassDel p) {
        try {
            JoinClass joinClass = joinClassRepository.findById(p.getJoinClassId()).orElseThrow(() -> new CustomException(AcaClassErrorCode.NOT_FOUND_JOIN_CLASS));
            joinClassRepository.delete(joinClass);
            return 1;
        } catch (CustomException e) {
            e.getMessage();
            return 0;
        }
    }
}
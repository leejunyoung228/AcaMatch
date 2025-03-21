package com.green.acamatch.joinClass;

import com.green.acamatch.acaClass.ClassRepository;
import com.green.acamatch.config.exception.AcaClassErrorCode;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.UserErrorCode;
import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.security.AuthenticationFacade;
import com.green.acamatch.entity.acaClass.AcaClass;
import com.green.acamatch.entity.joinClass.JoinClass;
import com.green.acamatch.entity.myenum.UserRole;
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
        try {
            List<JoinClassDto> result = mapper.selJoinClass(p);
            if (result == null || result.isEmpty()) {
                throw new CustomException(AcaClassErrorCode.FAIL_TO_SEL);
            }
            return result;
        } catch (CustomException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public int putJoinClass(JoinClassPutReq p) {
        try {
            JoinClass joinClass = joinClassRepository.findById(p.getJoinClassId()).orElseThrow(()
                    -> new CustomException(AcaClassErrorCode.NOT_FOUND_JOIN_CLASS));
            joinClass.setJoinClassId(p.getJoinClassId());

            if (p.getCertification() > 1) {
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
            JoinClass joinClass = joinClassRepository.findById(p.getJoinClassId()).orElseThrow(()
                    -> new CustomException(AcaClassErrorCode.NOT_FOUND_JOIN_CLASS));
            joinClassRepository.delete(joinClass);
            return 1;
        } catch (CustomException e) {
            e.getMessage();
            return 0;
        }
    }
}
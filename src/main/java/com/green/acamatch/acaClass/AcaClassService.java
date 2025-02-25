package com.green.acamatch.acaClass;

import com.green.acamatch.acaClass.model.*;
import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.joinClass.model.JoinClassRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AcaClassService {
    private final AcaClassMapper mapper;
    private final UserMessage userMessage;
    private final JoinClassRepository joinClassRepository;

    // 특정 학원의 특정 수업을 듣는 학생(또는 학부모) 목록 조회
    public List<User> findStudentsByClassId(Long classId) {
        return joinClassRepository.findStudentsByClassId(classId);
    }

    //수업 등록
    @Transactional
    public int postAcaClass(AcaClassPostReq p) {

        int exists = mapper.existsClass(p.getAcaId(), p.getClassName());
        if (exists > 0) {
            throw new IllegalArgumentException("이미 존재하는 강좌입니다.");
        }

        return mapper.insAcaClass(p);
    }

    //요일 등록
    @Transactional
    public int insWeekDay(AcaClassWeekDay p) {

        int exists = mapper.existsDay(p.getDay());
        if (exists > 0) {
            throw new IllegalArgumentException("이미 존재하는 요일입니다.");
        }
        return mapper.insWeekDay(p);
    }

    //개강날 등록
    @Transactional
    public int insAcaClassClassWeekDays(AcaClassWeekDaysRelation p) {

        int exists = mapper.existsClassWeekDays(p.getDayId(), p.getClassId());
        if (exists > 0) {
            throw new IllegalArgumentException("중복된 강좌 요일입니다.");
        }

        return mapper.insAcaClassClassWeekDays(p);
    }

    //카테고리 등록
    @Transactional
    public int insAcaClassCategory(AcaClassCategoryReq p) {

        int exists = mapper.existsCategory(p.getClassId(), p.getCategoryId());
        if(exists > 0) {
            throw new IllegalArgumentException("중복된 카테고리입니다.");
        }
        return mapper.insAcaClassCategory(p);
    }

    //수업 상세정보 불러오기
    public List<AcaClassDetailDto> getClass(AcaClassDetailGetReq p) {
        try {
            List<AcaClassDetailDto> result = mapper.selAcaClassDetail(p);
            if (result == null || result.isEmpty()) {
                userMessage.setMessage("상세정보 불러오기에 실패하였습니다.");
                return null;
            }
            userMessage.setMessage("상세정보 불러오기에 성공하였습니다.");
            return result;
        } catch (Exception e) {
            userMessage.setMessage("기타 오류 사항으로 상세정보를 불러오지 못했습니다.");
            return null;
        }
    }

    //특정 user가 등록한 class 가져오기
    public List<AcaClassUserDto> getClassUser(AcaClassUserGetReq p) {
        try {
            List<AcaClassUserDto> result = mapper.selAcaClassUser(p);
            if (result == null || result.isEmpty()) {
                userMessage.setMessage("등록한 강좌 정보 불러오기에 실패하였습니다.");
                return null;
            }
            userMessage.setMessage("등록한 강좌 정보 불러오기에 성공하였습니다.");
            return result;
        } catch (Exception e) {
            userMessage.setMessage("기타 오류 사항으로 정보를 불러오지 못했습니다.");
            return null;
        }
    }

    //학원 강좌 가져오기
    public List<AcaClassDto> selAcaClass(AcaClassGetReq p) {
        try {
            List<AcaClassDto> result = mapper.selAcaClass(p);
            if (result == null || result.isEmpty()) {
                userMessage.setMessage("학원 강좌 정보 불러오기에 실패하였습니다.");
                return null;
            }
            userMessage.setMessage("학원 강좌 정보 불러오기에 성공하였습니다.");
            return result;
        } catch (Exception e) {
            userMessage.setMessage("기타 오류 사항으로 정보를 불러오지 못했습니다.");
            return null;
        }
    }

    //강좌 수정
    public int updAcaClass(AcaClassPutReq p) {

        try {
            int result = mapper.updAcaClass(p);
            userMessage.setMessage("강좌 정보 수정에 성공하였습니다.");
            return result;
        } catch (BadSqlGrammarException e) {
            userMessage.setMessage("잘못된 형식을 입력하였습니다.");
            return 0;
        }
    }

    //class 삭제
    public int delAcaClass(AcaClassDelReq p) {
        int result = mapper.delAcaClass(p);

        if (result == 1) {
            userMessage.setMessage("강좌 삭제에 성공하였습니다.");
            return result;
        } else {
            userMessage.setMessage("강좌 삭제에 실패하였습니다.");
            return 0;
        }
    }

    // 수업이 열리는 요일 삭제하기
    public int delAcaClassDay(AcaClassWeekDaysRelation p) {
        int result = mapper.delAcaClassDay(p);

        if (result == 1) {
            userMessage.setMessage("개강날 삭제에 성공하였습니다.");
            return result;
        } else {
            userMessage.setMessage("개강날 삭제에 실패하였습니다.");
            return 0;
        }
    }
}
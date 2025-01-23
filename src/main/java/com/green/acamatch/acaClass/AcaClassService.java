package com.green.acamatch.acaClass;

import com.green.acamatch.acaClass.model.*;
import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.model.ResultResponse;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AcaClassService {
    private final AcaClassMapper mapper;
    private final UserMessage userMessage;

    //수업 등록
    @Transactional
    public int postAcaClass(AcaClassPostReq p) {

        int exists = mapper.existsClass(p.getAcaId(), p.getClassName());
        if (exists > 0) {
            throw new IllegalArgumentException("이미 존재하는 수업입니다.");
        }

        int result = mapper.insAcaClass(p);
        return result;
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
    public int insAcaClassClassWeekDays(AcaClassClassWeekDays p) {

        int exists = mapper.existsClassWeekDays(p.getDayId(), p.getClassId());
        if (exists > 0) {
            throw new IllegalArgumentException("중복된 수업 요일입니다.");
        }

        return mapper.insAcaClassClassWeekDays(p);
    }

    //수업 상세정보 불러오기
    public List<AcaClassDto> getClass(AcaClassGetReq p) {
        try {
            List<AcaClassDto> result = mapper.selAcaClass(p);
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

    //class에 등록한 user정보 가져오기
    public List<AcaClassToUserDto> getUserClass(AcaClassToUserGetReq p) {
        try {
            List<AcaClassToUserDto> result = mapper.selAcaClassToUser(p);
            if (result == null || result.isEmpty()) {
                userMessage.setMessage("수강생 정보 불러오기에 실패하였습니다.");
                return null;
            }
            userMessage.setMessage("수강생 정보 불러오기에 성공하였습니다.");
            return result;
        } catch (Exception e) {
            userMessage.setMessage("기타 오류 사항으로 정보를 불러오지 못했습니다.");
            return null;
        }
    }

    //특정 user가 등록한 class 가져오기
    public List<AcaClassUserDto> getClassUser(AcaClassUserGetReq p) {
        try {
            List<AcaClassUserDto> result = mapper.selAcaClassUser(p);
            if (result == null || result.isEmpty()) {
                userMessage.setMessage("등록한 수업 정보 불러오기에 실패하였습니다.");
                return null;
            }
            userMessage.setMessage("등록한 수업 정보 불러오기에 성공하였습니다.");
            return result;
        } catch (Exception e) {
            userMessage.setMessage("기타 오류 사항으로 정보를 불러오지 못했습니다.");
            return null;
        }
    }

    //class 수정
    public int updAcaClass(AcaClassPutReq p) {
        try {
            int result = mapper.updAcaClass(p);
            userMessage.setMessage("수업 정보 수정에 성공하였습니다.");
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
            userMessage.setMessage("수업 삭제에 성공하였습니다.");
            return result;
        } else {
            userMessage.setMessage("수업 삭제에 실패하였습니다.");
            return 0;
        }
    }

    // 수업이 열리는 요일 삭제하기
    public int delAcaClassDay(AcaClassClassWeekDays p) {
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
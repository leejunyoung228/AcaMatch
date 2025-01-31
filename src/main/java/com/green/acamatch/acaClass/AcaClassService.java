package com.green.acamatch.acaClass;

import com.green.acamatch.acaClass.model.*;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.ErrorCode;
import com.green.acamatch.config.exception.UserErrorCode;
import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.jwt.JwtUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static com.green.acamatch.config.exception.UserErrorCode.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class AcaClassService {
    private final AcaClassMapper mapper;
    private final UserMessage userMessage;

    /**  JWT에서 userId 가져오기 */
    private JwtUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()|
                authentication.getPrincipal().equals("anonymousUser")) {
            userMessage.setMessage("로그인이 필요합니다.");
            throw new CustomException(UserErrorCode.UNAUTHENTICATED);
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof JwtUser) {
            return (JwtUser) principal;
        } else if (principal instanceof String) {
            try {
                return new JwtUser(Long.parseLong((String) principal), Collections.emptyList());
            } catch (NumberFormatException e) {
                userMessage.setMessage("잘못된 인증 정보입니다.");
                throw new CustomException(UserErrorCode.UNAUTHENTICATED);
            }
        } else {
            userMessage.setMessage("알 수 없는 사용자 타입입니다.");
            throw new CustomException(UserErrorCode.UNAUTHENTICATED);
        }
    }

    /**  로그인된 사용자 검증 */
    private void validateAuthenticatedUser(long requestUserId) {
        long jwtUserId = getAuthenticatedUser().getSignedUserId();

        //  사용자 존재 여부 체크 추가
        validateUserExists(jwtUserId);

        if (jwtUserId != requestUserId) {
            throw new CustomException(USER_NOT_FOUND.UNAUTHENTICATED);
        }
    }

    private void validateUserExists(long jwtUserId) {
    }

    //수업 등록
    @Transactional
    public int postAcaClass(AcaClassPostReq p) {

        int exists = mapper.existsClass(p.getAcaId(), p.getClassName());
        if (exists > 0) {
            throw new IllegalArgumentException("이미 존재하는 강좌입니다.");
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
    public int insAcaClassClassWeekDays(AcaClassWeekDaysRelation p) {

        int exists = mapper.existsClassWeekDays(p.getDayId(), p.getClassId());
        if (exists > 0) {
            throw new IllegalArgumentException("중복된 강좌 요일입니다.");
        }

        return mapper.insAcaClassClassWeekDays(p);
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
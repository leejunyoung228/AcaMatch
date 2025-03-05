package com.green.acamatch.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AcaClassErrorCode implements ErrorCode {
    NOT_FOUND_CLASS(HttpStatus.NOT_FOUND, "강좌를 찾을 수 없습니다."),
    NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),
    FAIL_TO_UPD(HttpStatus.BAD_REQUEST, "강좌 수정에 실패하였습니다."),
    FAIL_TO_SEL(HttpStatus.BAD_REQUEST, "성적확인을 위한 학원명/강좌명 불러오기에 실패하였습니다."),
    NOT_FOUND_DAY(HttpStatus.NOT_FOUND, "요일을 찾을 수 없습니다."),
    INVALID_DAY_FOR_CLASS(HttpStatus.BAD_REQUEST, "강좌가 열리는 날이 아닙니다."),
    NOT_FOUND_JOIN_CLASS(HttpStatus.NOT_FOUND, "수강생을 찾을 수 없습니다."),
    EXISTS_STATUS(HttpStatus.BAD_REQUEST, "이미 처리 되었습니다."),
    NOT_FOUND_ATTENDANCE(HttpStatus.NOT_FOUND, "출석부를 찾을 수 없습니다."),
    NOT_FOUND_EXAM(HttpStatus.NOT_FOUND, "시험을 찾을 수 없습니다."),
    NOT_FOUND_GRADE(HttpStatus.NOT_FOUND, "성적을 찾을 수 없습니다."),
    EXISTS_TEACHER(HttpStatus.BAD_REQUEST, "이미 강좌에 등록된 선생님이 있습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
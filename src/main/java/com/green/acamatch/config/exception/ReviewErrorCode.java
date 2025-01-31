package com.green.acamatch.config.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ReviewErrorCode implements ErrorCode {
    INVALID_ACADEMY(HttpStatus.BAD_REQUEST, "유효하지 않은 학원 ID입니다."),
    INVALID_USER(HttpStatus.BAD_REQUEST, "유효하지 않은 유저 ID입니다."),
    UNRIGHT_USER(HttpStatus.UNAUTHORIZED, "해당 리뷰의 작성자가 아닙니다. 삭제할 권한이 없습니다."),
    UNAUTHORIZED_ACADEMY_ACCESS(HttpStatus.FORBIDDEN, "해당 학원을 관리할 권한이 없습니다."),
    UNAUTHENTICATED_USER(HttpStatus.UNAUTHORIZED, "로그인한 사용자 정보가 일치하지 않습니다."),
    CONFLICT_REVIEW_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 리뷰입니다.")

    ;

    private final HttpStatus httpStatus;
    private final String message;

    // 생성자 명시적 선언
    ReviewErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}



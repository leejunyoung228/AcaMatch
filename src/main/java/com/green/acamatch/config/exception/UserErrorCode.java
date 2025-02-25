package com.green.acamatch.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    INCORRECT_ID_PW(HttpStatus.BAD_REQUEST, "아이디, 비밀번호를 확인해 주세요."),
    INCORRECT_PW(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    DUPLICATE_USER_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일 입니다."),
    DUPLICATE_USER_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임 입니다."),
    INCORRECT_DUPLICATE_CHECK_TYPE(HttpStatus.BAD_REQUEST, "지정된 타입이 아닙니다."),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "로그인을 해주세요."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    //SNS 로그인 계정일 경우 예외 코드 추가
    USE_SOCIAL_LOGIN(HttpStatus.BAD_REQUEST, "SNS 로그인 계정입니다. 소셜 로그인을 이용해 주세요.");

    private final HttpStatus httpStatus;
    private final String message;
}

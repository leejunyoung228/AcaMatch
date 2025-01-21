package com.green.acamatch.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

//500단위 에러 서버측 에러
//400단위 에러 클라이언트측 에러
@Getter
@RequiredArgsConstructor
public enum EmailErrorCode implements ErrorCode {

     EMAIL_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송을 실패하였습니다.")
    , EMAIL_ALREADY_SENT(HttpStatus.CONFLICT, "이메일이 이미 전송되었습니다.")
    , EXPIRED_OR_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "token이 유효하지 않거나 만료되었습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}

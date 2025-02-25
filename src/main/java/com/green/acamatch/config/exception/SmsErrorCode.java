package com.green.acamatch.config.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public enum SmsErrorCode implements ErrorCode {
    SMS_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "SMS 전송에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    SmsErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
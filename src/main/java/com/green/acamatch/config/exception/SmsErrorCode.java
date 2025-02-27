package com.green.acamatch.config.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public enum SmsErrorCode implements ErrorCode {
    SMS_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "SMS 전송에 실패했습니다."),
    INVALID_SENDER(HttpStatus.BAD_REQUEST, "발신자 정보가 없습니다."),
    INVALID_RECEIVER(HttpStatus.BAD_REQUEST, "수신자 정보가 없습니다."),
    EMPTY_MESSAGE(HttpStatus.BAD_REQUEST, "메시지 내용이 없습니다."),
    SERVICE_NOT_AVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "SMS 서비스가 사용 불가능합니다."),
    INVALID_MESSAGE_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 메시지 요청입니다."),
    INVALID_CLASS(HttpStatus.BAD_REQUEST, "유효하지 않은 classId")
    ;

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

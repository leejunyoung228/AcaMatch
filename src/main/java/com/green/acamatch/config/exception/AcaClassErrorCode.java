package com.green.acamatch.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AcaClassErrorCode implements ErrorCode {
    NOT_FOUND_CLASS(HttpStatus.BAD_REQUEST, "강좌를 찾을 수 없습니다."),
    NOT_FOUND_CATEGORY(HttpStatus.BAD_REQUEST, "카테고리를 찾을 수 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}

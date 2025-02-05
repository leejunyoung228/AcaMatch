package com.green.acamatch.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AcaClassErrorCode implements ErrorCode {

    ;

    private final HttpStatus httpStatus;
    private final String message;
}

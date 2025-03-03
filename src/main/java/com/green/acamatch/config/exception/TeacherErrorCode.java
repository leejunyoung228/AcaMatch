package com.green.acamatch.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TeacherErrorCode implements ErrorCode {
    NOT_FOUND_TEACHER(HttpStatus.NOT_FOUND, "선생님을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
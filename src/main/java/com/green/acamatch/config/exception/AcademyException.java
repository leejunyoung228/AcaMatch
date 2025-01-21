package com.green.acamatch.config.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AcademyException implements ErrorCode{
    Missing_Required_Field_Exception (HttpStatus.BAD_REQUEST, "필수값을 제대로 입력하지 않았습니다.")

    ;

    private final HttpStatus httpStatus;
    private final String message;
}

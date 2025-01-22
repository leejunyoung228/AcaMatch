package com.green.acamatch.config.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AcademyException implements ErrorCode{
    MISSING_REQUIRED_FILED_EXCEPTION (HttpStatus.BAD_REQUEST, "필수값을 제대로 입력하지 않았습니다."),
    DUPLICATE_TAG(HttpStatus.BAD_REQUEST, "태그가 중복 선택되었습니다."),
    PHOTO_SAVE_FAILED(HttpStatus.BAD_REQUEST, "사진 형식이 올바르지 않습니다."),
    INVALID_FORMAT(HttpStatus.BAD_REQUEST, "입력 형식이 잘못되었습니다."),
    MISSING_UPDATE_FILED_EXCEPTION(HttpStatus.BAD_REQUEST, "값을 입력하지 않았습니다.")

    ;

    private final HttpStatus httpStatus;
    private final String message;
}

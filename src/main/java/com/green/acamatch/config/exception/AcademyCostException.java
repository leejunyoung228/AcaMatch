package com.green.acamatch.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AcademyCostException implements ErrorCode {
    PAY_CANCEL (HttpStatus.BAD_REQUEST, "주문을 취소하였습니다."),
    PAY_FAILED (HttpStatus.BAD_REQUEST, "주문에 실패하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

package com.green.acamatch.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RelationshipErrorCode implements ErrorCode {
    REQUEST_RELATIONSHIP_NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 관계를 찾을수 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}

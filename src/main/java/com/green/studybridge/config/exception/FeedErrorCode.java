package com.green.studybridge.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FeedErrorCode implements ErrorCode {
   FAIL_TO_REQ(HttpStatus.INTERNAL_SERVER_ERROR, "피드 등록에 실패했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}

package com.green.acamatch.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum popUpErrorCode implements ErrorCode{
    POPUP_NOT_FOUND(HttpStatus.NOT_FOUND, "팝업을 찾을 수 없습니다.")
    ,FAIL_TO_UPD(HttpStatus.BAD_REQUEST, "팝업 수정에 실패하였습니다.")
    ;

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return null;
    }
}

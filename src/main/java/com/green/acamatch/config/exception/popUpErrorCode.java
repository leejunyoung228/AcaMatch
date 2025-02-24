package com.green.acamatch.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum popUpErrorCode implements ErrorCode{
     POPUP_NOT_FOUND(HttpStatus.NOT_FOUND, "팝업을 찾을 수 없습니다.")
    ,FAIL_TO_UPD(HttpStatus.BAD_REQUEST, "팝업 수정에 실패하였습니다.")
    ,COMMENT_OR_PHOTO_REQUIRED(HttpStatus.BAD_REQUEST, "글,사진 둘 중 하나는 입력해야합니다.")
    ;

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return null;
    }
}

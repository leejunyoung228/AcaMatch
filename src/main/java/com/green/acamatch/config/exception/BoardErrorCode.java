package com.green.acamatch.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardErrorCode implements ErrorCode {
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "공지사항을 찾을 수 없습니다.")
   ,FAIL_TO_REG(HttpStatus.BAD_REQUEST, "공지사항 등록에 실패하였습니다.")
   ,FAIL_TO_UPD(HttpStatus.BAD_REQUEST, "공지사항 수정에 실패하였습니다.")
   ,FAIL_TO_DEL(HttpStatus.BAD_REQUEST, "공지사항 삭제에 실패하였습니다.")
    ;

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return null;
    }
}

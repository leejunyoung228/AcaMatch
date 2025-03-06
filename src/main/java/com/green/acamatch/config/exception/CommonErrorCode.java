package com.green.acamatch.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
//500단위 에러 서버측 에러
//400단위 에러 클라이언트측 에러
@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

      INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR
                            , "서버 내부에서 에러가 발생하였습니다.")
    , INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 파라미터입니다.")
    , REQUEST_URL_NOT_FOUND(HttpStatus.NOT_FOUND, "Request Url is Not Found.")
    , COOKIE_NOT_FOUND(HttpStatus.BAD_REQUEST, "쿠키를 찾을수 없습니다.")
    , MISSING_REQUIRED_FILED_EXCEPTION(HttpStatus.BAD_REQUEST, "파일을 하나 이상 업로드해야 합니다.")
    , FILE_UPLOAD_FAILED(HttpStatus.BAD_REQUEST, "파일 저장에 실패했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}

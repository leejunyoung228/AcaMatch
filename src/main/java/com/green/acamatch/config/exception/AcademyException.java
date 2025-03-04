package com.green.acamatch.config.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AcademyException implements ErrorCode{
    NOT_FOUND_ACADEMY(HttpStatus.BAD_REQUEST, "학원을 찾을 수 없습니다."),
    MISSING_REQUIRED_FILED_EXCEPTION (HttpStatus.BAD_REQUEST, "필수값을 제대로 입력하지 않았습니다."),
    DUPLICATE_TAG(HttpStatus.BAD_REQUEST, "태그가 중복 선택되었습니다."),
    PHOTO_SAVE_FAILED(HttpStatus.BAD_REQUEST, "사진 형식이 올바르지 않습니다."),
    INVALID_FORMAT(HttpStatus.BAD_REQUEST, "입력 형식이 잘못되었습니다."),
    MISSING_UPDATE_FILED_EXCEPTION(HttpStatus.BAD_REQUEST, "수정할 값을 하나 이상 입력해주세요."),
    NO_SUCH_ELEMENT_EXCEPTION(HttpStatus.NOT_FOUND, "값을 가져올수 없습니다."),
    ILLEGAL_ARGUMENT_EXCEPTION(HttpStatus.BAD_REQUEST, "주소를 수정하려면 주소와 관련된 값을 다 입력해주세요."),
    NOT_FOUND_BUSINESSNUMBER(HttpStatus.NOT_FOUND, "조회할 수 없는 사업자등록번호입니다."),
    DATA_EXISTS(HttpStatus.BAD_REQUEST, "배너가 이미 존재합니다.");


    private final HttpStatus httpStatus;
    private final String message;
}

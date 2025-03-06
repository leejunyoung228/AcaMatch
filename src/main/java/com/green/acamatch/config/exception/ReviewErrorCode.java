package com.green.acamatch.config.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ReviewErrorCode implements ErrorCode {
    INVALID_ACADEMY(HttpStatus.BAD_REQUEST, "유효하지 않은 학원 ID입니다."),
    INVALID_USER(HttpStatus.BAD_REQUEST, "유효하지 않은 유저 ID입니다."),
    UNRIGHT_USER(HttpStatus.UNAUTHORIZED, "해당 리뷰의 작성자가 아닙니다. 삭제할 권한이 없습니다."),
    UNAUTHORIZED_ACADEMY_ACCESS(HttpStatus.FORBIDDEN, "해당 학원을 관리할 권한이 없습니다."),
    ONLY_PARENT_CAN_WRITE(HttpStatus.FORBIDDEN, "보호자만 학생 대신 리뷰를 남길 수 있습니다."),
    UNAUTHENTICATED_USER(HttpStatus.UNAUTHORIZED, "로그인한 사용자 정보가 일치하지 않습니다."),
    CONFLICT_REVIEW_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 리뷰입니다."),
    NOT_ACADEMY_MANAGER(HttpStatus.FORBIDDEN, "학원 관계자가 아닙니다. 권한이 없습니다."),
    STUDENT_NOT_IN_CLASS(HttpStatus.BAD_REQUEST, "해당 학생은 해당 수업에 등록되어 있지 않습니다."),
    NOT_STUDENT_PARENT(HttpStatus.FORBIDDEN, "해당 보호자와 학생의 관계가 존재하지 않습니다."),
    INVALID_REVIEW_DATA(HttpStatus.BAD_REQUEST, "리뷰 데이터가 유효하지 않습니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 리뷰를 찾을 수 없습니다."),
    CLASS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 수업을 찾을 수 없습니다."),
    JOIN_CLASS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 수업을 들은 정보가 없습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "리뷰를 쓸 권한이 없습니다."),
    IMAGE_UPLOAD_FAILED(HttpStatus.ACCEPTED, "사진 올리기를 실패하였습니다."),
    UNAUTHORIZED_PARENT(HttpStatus.FORBIDDEN, "부모-학생 관계가 인증되지 않았습니다.")
    ;

    private final HttpStatus status;
    private final String message;

    ReviewErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return status;
    }
}

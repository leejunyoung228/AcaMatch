package com.green.acamatch.config.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum ManagerErrorCode implements ErrorCode {
    // 🔹 일반적인 권한 관련 오류
    PERMISSION_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "인증되지 않은 접근입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    // 🔹 관리자 관련 오류
    MANAGER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 관리자를 찾을 수 없습니다."),
    MANAGER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 등록된 관리자입니다."),
    MANAGER_ROLE_REQUIRED(HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다."),

    // 🔹 학원 관련 오류
    ACADEMY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 학원을 찾을 수 없습니다."),
    ACADEMY_ACCESS_DENIED(HttpStatus.FORBIDDEN, "이 학원을 관리할 수 있는 권한이 없습니다."),
    ACADEMY_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 등록된 학원입니다."),

    // 🔹 선생님 관련 오류
    TEACHER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 선생님을 찾을 수 없습니다."),
    TEACHER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "이 선생님을 관리할 수 있는 권한이 없습니다."),
    TEACHER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 등록된 선생님입니다."),

    // 🔹 수업(Class) 관련 오류
    CLASS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 수업을 찾을 수 없습니다."),
    CLASS_ACCESS_DENIED(HttpStatus.FORBIDDEN, "이 수업을 관리할 수 있는 권한이 없습니다."),
    CLASS_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 등록된 수업입니다."),

    // 🔹 기타
    INVALID_MANAGER_OPERATION(HttpStatus.BAD_REQUEST, "잘못된 관리자 작업입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ManagerErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    // ✅ 인터페이스 메서드 구현 (오류 해결)
    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

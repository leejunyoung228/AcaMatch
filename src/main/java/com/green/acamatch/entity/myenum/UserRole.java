package com.green.acamatch.entity.myenum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    ADMIN(0),
    STUDENT(1),
    PARENT(2),
    ACADEMY(3),
    TEACHER(4),
    NOT_SELECTED(5);

    private final int value;

    // value를 키로 하는 Map
    private static final Map<Integer, UserRole> valueMap = Arrays.stream(UserRole.values())
            .collect(Collectors.toMap(UserRole::getValue, role -> role));

    // name을 키로 하는 Map
    private static final Map<String, UserRole> nameMap = Arrays.stream(UserRole.values())
            .collect(Collectors.toMap(UserRole::name, role -> role));

    @JsonValue
    public int getValue() {
        return value;
    }

    @JsonCreator
    public static UserRole fromJson(Object input) {
        if (input == null) {
            // null일 경우 기본값 반환 (예: STUDENT)
            return UserRole.STUDENT;
        } else if (input instanceof Number) {
            int intValue = ((Number) input).intValue();
            return valueMap.get(intValue);  // Map에서 바로 조회
        } else if (input instanceof String strValue) {
            return nameMap.get(strValue.toUpperCase());  // 대소문자 무시하고 Map에서 바로 조회
        }
        throw new IllegalArgumentException("Invalid UserRole input: " + input);
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public boolean isTeacher() {
        return this == TEACHER;
    }

    public boolean isAcademy() {
        return this == ACADEMY;
    }

    // 관리자(ADMIN), 선생(TEACHER), 학원(Academy)만 가능
    public boolean isAdminOrTeacherOrAcademy() {
        return isAdmin() || isTeacher() || isAcademy();
    }

}

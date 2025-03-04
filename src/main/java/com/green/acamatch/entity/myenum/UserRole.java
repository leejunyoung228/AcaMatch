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
    public static UserRole fromJson(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("UserRole cannot be null or empty");
        }

        // 숫자 값 변환 시도
        try {
            int numericValue = Integer.parseInt(input);
            UserRole role = valueMap.get(numericValue);
            if (role != null) {
                return role;
            }
        } catch (NumberFormatException ignored) { }

        // 문자열 변환 시도 (대소문자 무시)
        UserRole role = nameMap.get(input.toUpperCase());
        if (role != null) {
            return role;
        }

        throw new IllegalArgumentException("Invalid UserRole input: " + input);
    }


    // 추가된 개별 역할 확인 메서드
    public boolean isAdmin() {
        return this == ADMIN;
    }

    public boolean isTeacher() {
        return this == TEACHER;
    }

    public boolean isAcademy() {
        return this == ACADEMY;
    }

    // 관리자, 선생님, 학원 역할 확인 (기존 유지)
    public boolean isAdminOrTeacherOrAcademy() {
        return isAdmin() || isTeacher() || isAcademy();
    }
}

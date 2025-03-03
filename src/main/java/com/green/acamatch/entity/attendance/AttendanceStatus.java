package com.green.acamatch.entity.attendance;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AttendanceStatus {
    PRESENT("출석"),
    LATE("지각"),
    ABSENT("결석"),
    EARLY_LEAVE("조퇴");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    // 한글 값으로 Enum 찾기
    public static AttendanceStatus fromValue(String value) {
        for (AttendanceStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("잘못된 출석 상태: " + value);
    }
}
package com.green.acamatch.entity.reports;

import com.fasterxml.jackson.annotation.JsonValue;
import com.green.acamatch.entity.attendance.AttendanceStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public enum ReportsType {
    스팸("스팸홍보/도배"),
    음란물("음란물"),
    불법정보포함("불법정보 포함"),
    청소년유해내용("청소년 유해 내용"),
    욕설생명경시혐오차별적표현("욕설/생명경시/혐오/차별적 표현"),
    개인정보노출("개인정보 노출"),
    불쾌한표현("불쾌한 표현"),
    명예훼손또는저작권침해("명예훼손 또는 저작권 침해"),
    불법촬영물포함("불법촬영물 포함");

    private final String description;

    public static ReportsType fromValue(String description) {
        for (ReportsType status : values()) {
            if (status.description.equals(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("reportsType : " + description);
    }
}

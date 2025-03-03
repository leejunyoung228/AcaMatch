package com.green.acamatch.entity.attendance;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AttendanceStatusConverter implements AttributeConverter<AttendanceStatus, String> {
        @Override
        public String convertToDatabaseColumn(AttendanceStatus status) {
            if (status == null) return null;
            return status.getValue(); // 한글로 변환하여 DB 저장
        }

        @Override
        public AttendanceStatus convertToEntityAttribute(String dbData) {
            if (dbData == null) return null;
            return AttendanceStatus.fromValue(dbData); // 한글을 Enum으로 변환
 }
}
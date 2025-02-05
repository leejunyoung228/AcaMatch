package com.green.acamatch.chat;

import com.green.acamatch.config.exception.CommonErrorCode;
import com.green.acamatch.config.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SenderType {
    USER_TO_ACADEMY(0),
    ACADEMY_TO_USER(1);
    private final int value;
    public static SenderType fromValue(int value) {
        for (SenderType type : SenderType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new CustomException(CommonErrorCode.INVALID_PARAMETER);
    }
}

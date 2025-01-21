package com.green.acamatch.config.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class UserMessage {
    private String message;

    // 메시지 설정 메서드
    public void setMessage(String message) {
        this.message = message;
    }

    // 메시지 가져오기 메서드
    public String getMessage() {
        return message;
    }
}
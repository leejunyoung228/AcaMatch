package com.green.acamatch.sms.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsRequest {
    private String to;
    private String text;
    private Long classId; // classId 추가
}

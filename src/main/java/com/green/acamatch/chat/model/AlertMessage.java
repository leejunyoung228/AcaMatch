package com.green.acamatch.chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlertMessage {
    private long receiverId;
    private String  message;
}

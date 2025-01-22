package com.green.acamatch.chat.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatLogRes{
    private String acaName;
    private String message;
    private int senderType;
    private LocalDateTime createdAt;
}

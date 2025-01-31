package com.green.acamatch.chat.model;

import com.green.acamatch.entity.academy.Chat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatLogRes{
    private String userName;
    private String acaName;
    private String message;
    private int senderType;
    private int isRead;
    private LocalDateTime createdAt;

    public ChatLogRes(Chat chat){
        this.userName = chat.getUser().getName();
        this.acaName = chat.getAcademy().getAcaName();
        this.message = chat.getMessage();
        this.senderType = chat.getSenderType();
        this.isRead = chat.getIsRead();
        this.createdAt = chat.getCreatedAt();
    }
}

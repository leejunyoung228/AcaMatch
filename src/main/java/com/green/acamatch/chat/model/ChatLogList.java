package com.green.acamatch.chat.model;

import com.green.acamatch.entity.myenum.SenderType;
import com.green.acamatch.entity.academy.Chat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatLogList {
    private Long chatId;
    private String userName;
    private String acaName;
    private String message;
    private SenderType senderType;
    private boolean isRead;
    private LocalDateTime createdAt;

    public ChatLogList(Chat chat){
        this.chatId = chat.getChatId();
//        this.userName = chat.getUser().getName();
//        this.acaName = chat.getAcademy().getAcaName();
        this.message = chat.getMessage();
        this.senderType = chat.getSenderType();
        this.isRead = chat.isRead();
        this.createdAt = chat.getCreatedAt();
    }
}

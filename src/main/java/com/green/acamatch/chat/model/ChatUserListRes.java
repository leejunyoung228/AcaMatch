package com.green.acamatch.chat.model;

import com.green.acamatch.entity.academy.Chat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatUserListRes {
    private long userId;
    private String userName;
    private long acaId;
    private String acaName;

    public ChatUserListRes(Chat chat) {
        this.userId = chat.getUser().getUserId();
        this.userName = chat.getUser().getName();
        this.acaId = chat.getAcademy().getAcaId();
        this.acaName = chat.getAcademy().getAcaName();
    }
}

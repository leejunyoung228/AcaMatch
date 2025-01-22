package com.green.acamatch.chat.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatUserListRes {
    private long userId;
    private String name;
    private long acaId;
    private String acaName;
}

package com.green.acamatch.chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetChatRoomIdReq {
    private long acaId;
    private long userId;
}

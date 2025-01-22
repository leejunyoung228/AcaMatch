package com.green.acamatch.chat.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ChatSendReq {
    private long userId;
    private long acaId;
    @Schema(description = "0: user-> aca, 1: aca -> user")
    private int senderType;
    private String message;
}

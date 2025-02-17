package com.green.acamatch.chat.model;

import com.green.acamatch.entity.myenum.SenderType;
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
    private long chatRoomId;
    @Schema(description = "0: user-> aca, 1: aca -> user")
    private SenderType senderType;
    private String message;
}

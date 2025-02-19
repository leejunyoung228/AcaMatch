package com.green.acamatch.chat.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetChatRoomIdReq {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private long acaId;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private long userId;
}

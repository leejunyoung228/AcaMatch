package com.green.acamatch.chat.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.BindParam;

@Getter
@Setter
public class GetChatRoomIdReq {
    @Schema(name = "aca-id", requiredMode = Schema.RequiredMode.REQUIRED)
    private long acaId;
    @Schema(name = "user-id", requiredMode = Schema.RequiredMode.REQUIRED)
    private long userId;
    public GetChatRoomIdReq(@BindParam("user-id") long userId, @BindParam("aca-id") long acaId) {
        this.userId = userId;
        this.acaId = acaId;
    }
}

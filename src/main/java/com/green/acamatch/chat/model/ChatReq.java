package com.green.acamatch.chat.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.web.bind.annotation.BindParam;

@Getter
public class ChatReq extends Paging {
    @Schema(name = "user-id")
    private Long userId;
    @Schema(name = "aca-id")
    private Long acaId;


    public ChatReq(Integer page, Integer size, @BindParam("user-id") Long userId, @BindParam("aca-id") Long acaId) {
        super(page, size);
        this.userId = userId;
        this.acaId = acaId;
    }
}

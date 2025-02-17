package com.green.acamatch.chat.model;

import com.green.acamatch.config.model.Paging;
import com.green.acamatch.entity.myenum.SenderType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.web.bind.annotation.BindParam;

@Getter
@EqualsAndHashCode(callSuper = false)
public class ChatRoomGetReq extends Paging {
    @Schema(name = "user-id")
    private Long userId;
    @Schema(name = "aca-id")
    private Long acaId;
    private SenderType senderType;


    public ChatRoomGetReq(Integer page, Integer size, @BindParam("user-id") Long userId, @BindParam("aca-id") Long acaId) {
        super(page, size);
        this.userId = userId;
        this.acaId = acaId;
        this.senderType = userId != null ? SenderType.ACADEMY_TO_USER : SenderType.ACADEMY_TO_USER;
    }
}

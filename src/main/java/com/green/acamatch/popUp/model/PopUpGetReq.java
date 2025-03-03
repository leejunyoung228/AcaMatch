package com.green.acamatch.popUp.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PopUpGetReq extends Paging {
//    @Schema(title = "팝업 PK", requiredMode = Schema.RequiredMode.REQUIRED)
//    private long popUpId;

    public PopUpGetReq(Integer page, Integer size) {
        super(page, size);
    }
}
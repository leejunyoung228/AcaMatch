package com.green.acamatch.popUp.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PopUpGetReq {
    @Schema(title = "팝업 PK", requiredMode = Schema.RequiredMode.REQUIRED)
    private long popUpId;
}
package com.green.acamatch.academy.model.HB;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetDongReq {
    @Schema(title = "도시 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long cityId;

    @Schema(title = "시/군/구 PK", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private long streetId;
}

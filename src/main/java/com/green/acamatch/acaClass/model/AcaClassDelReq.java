package com.green.acamatch.acaClass.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AcaClassDelReq {
    @Schema(title = "강좌 PK", example = "144", requiredMode = Schema.RequiredMode.REQUIRED)
    private long classId;
    @Schema(title = "학원 PK", example = "1611", requiredMode = Schema.RequiredMode.REQUIRED)
    private long acaId;
}

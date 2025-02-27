package com.green.acamatch.joinClass.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinClassPutReq {
    @Schema(title = "joinClass PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long joinClassId;
    @Schema(title = "인가 여부", example = "1")
    private int certification;
}

package com.green.acamatch.acaClass.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AcaClassCategoryReq {
    @Schema(title = "수업 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long classId;
    @Schema(title = "카테고리 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long categoryId;
}

package com.green.acamatch.academy.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetCategorySearchReq {
    @Schema(title = "카테고리 Type PK", example = "1~2", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Integer> categoryTypeId;

    @Schema(title = "카테고리 PK", example = "1~9", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Long> categoryId;
}

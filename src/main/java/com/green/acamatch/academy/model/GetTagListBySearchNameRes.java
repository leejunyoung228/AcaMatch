package com.green.acamatch.academy.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTagListBySearchNameRes {
    @Schema(title = "태그 PK", example = "1")
    private long tagId;

    @Schema(title = "태그 이름", example = "초등 영어")
    private String tagName;
}

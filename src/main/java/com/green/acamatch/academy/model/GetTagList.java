package com.green.acamatch.academy.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTagList {
    @Schema(title = "태그 이름", example = "수학")
    private String tagName;
}

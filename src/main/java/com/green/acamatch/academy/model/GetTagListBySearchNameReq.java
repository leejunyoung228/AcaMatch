package com.green.acamatch.academy.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTagListBySearchNameReq {
    @Schema(title = "검색어", example = "영어")
    private String tagName;
}

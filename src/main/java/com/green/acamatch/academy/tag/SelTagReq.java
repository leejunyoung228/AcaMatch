package com.green.acamatch.academy.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelTagReq {
    @Schema(example = "수학")
    private String searchTag;
}

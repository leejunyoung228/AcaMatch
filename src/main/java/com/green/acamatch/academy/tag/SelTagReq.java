package com.green.acamatch.academy.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class SelTagReq {
    @Schema(example = "수학")
    private String searchTag;
}

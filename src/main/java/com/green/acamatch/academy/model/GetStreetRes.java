package com.green.acamatch.academy.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetStreetRes {
    @Schema(title = "시/군/구 PK", example = "10")
    private long streetId;

    @Schema(title = "시/군/구 이름", example = "도봉구")
    private String streetName;
}

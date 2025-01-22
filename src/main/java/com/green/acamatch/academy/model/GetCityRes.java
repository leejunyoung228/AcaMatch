package com.green.acamatch.academy.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetCityRes {
    @Schema(title = "도시 PK", example = "1")
    private long cityId;

    @Schema(title = "도시 이름", example = "서울 특별시")
    private String cityName;
}

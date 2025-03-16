package com.green.acamatch.manager.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserCountReq {
    @Schema(example = "2025")
    private String year;

    @Schema(example = "3")
    private String month;
}

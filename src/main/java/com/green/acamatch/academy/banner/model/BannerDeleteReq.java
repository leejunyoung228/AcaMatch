package com.green.acamatch.academy.banner.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BannerDeleteReq {
    @Schema(title = "학원pk")
    private Long acaId;

    @Schema(title = "배너 포지션", description = "1 or 2 or 3 or 4")
    private int bannerPosition;
}

package com.green.acamatch.academy.banner.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BannerUpdateReq {
    @Schema(title = "학원pk")
    private Long acaId;
    @Schema(title = "배너 포지션")
    private int  bannerPosition;
}

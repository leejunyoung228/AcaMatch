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
public class BannerTypeUpdateReq {
    @Schema(title = "학원pk")
    private Long acaId;
    @Schema(title = "배너승인여부")
    private int bannerType;
}

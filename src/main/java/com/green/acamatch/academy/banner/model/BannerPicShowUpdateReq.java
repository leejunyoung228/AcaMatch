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
public class BannerPicShowUpdateReq {
    @Schema(title = "학원pk")
    private Long acaId;
    @Schema(title = "배너위치", description = "상: 1, 하: 2, 좌: 3, 우: 4")
    private int bannerPosition;
    @Schema(title = "배너 활성화/비활성화", description = "활성화: 1, 비활성화: 0")
    private int bannerShow;
}

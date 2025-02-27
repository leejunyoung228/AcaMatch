package com.green.acamatch.academy.banner.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BannerByPositionGetReq {
    private Long acaId;
    private int bannerPosition;
}

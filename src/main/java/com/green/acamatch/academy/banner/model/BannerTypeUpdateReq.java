package com.green.acamatch.academy.banner.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BannerTypeUpdateReq {
    private Long acaId;
    private int bannerType;
}

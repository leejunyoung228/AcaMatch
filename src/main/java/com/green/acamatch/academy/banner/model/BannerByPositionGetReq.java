package com.green.acamatch.academy.banner.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.framework.qual.EnsuresQualifier;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BannerByPositionGetReq {
    private Long acaId;
    private int bannerPosition;
}

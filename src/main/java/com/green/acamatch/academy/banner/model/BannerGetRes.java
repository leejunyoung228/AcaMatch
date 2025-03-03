package com.green.acamatch.academy.banner.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class BannerGetRes {
    private Long acaId;
    private String acaName;
    private int bannerType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String bannerPic;
    private int bannerPosition;
    private int bannerShow;
}

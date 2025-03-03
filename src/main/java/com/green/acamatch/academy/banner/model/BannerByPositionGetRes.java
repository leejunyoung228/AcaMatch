package com.green.acamatch.academy.banner.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class BannerByPositionGetRes {
    private Long acaId;
    private String acaName;
    private int bannerType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String bannerPic;
    private int bannerPosition;
    private int bannerShow;
}

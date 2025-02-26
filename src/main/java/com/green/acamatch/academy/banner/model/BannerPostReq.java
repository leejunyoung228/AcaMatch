package com.green.acamatch.academy.banner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.acamatch.entity.myenum.BannerPosition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class BannerPostReq {
    @JsonIgnore
    private Long bannerId;

    private Long acaId;

    private String bannerPic;

    private LocalDate startDate;

    private LocalDate endDate;

    private String acaName;

    private BannerPosition bannerPosition;
}

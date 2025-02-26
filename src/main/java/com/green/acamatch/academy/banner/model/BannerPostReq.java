package com.green.acamatch.academy.banner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.acamatch.entity.myenum.BannerPosition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class BannerPostReq {
    @JsonIgnore
    @Schema(title = "배너 Pk")
    private Long bannerId;

    @Schema(title = "학원 pk")
    private Long acaId;

    @Schema(title = "배너 사진")
    private String bannerPic;

    @JsonIgnore
    @Schema(title = "배너승인여부")
    private String bannerType;

    @JsonIgnore
    @Schema(title = "배너 활성화")
    private String bannerShow;

    @JsonIgnore
    @Schema(title = "배너 시작일")
    private LocalDate startDate;

    @JsonIgnore
    @Schema(title = "배너 종료일")
    private LocalDate endDate;

    @Schema(title = "학원이름")
    private String acaName;

    @Schema(title = "배너위치")
    private BannerPosition bannerPosition;
}

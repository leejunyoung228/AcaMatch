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
    @Schema(title = "학원 pk")
    private Long acaId;
}

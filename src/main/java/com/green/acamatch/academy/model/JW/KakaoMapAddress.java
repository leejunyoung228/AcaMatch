package com.green.acamatch.academy.model.JW;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class KakaoMapAddress {
    private Long cityId;
    private Long streetId;

    private String cityName;
    private String streetName;
    private String dongName;

    private Double latitude;
    private Double longitude;
}

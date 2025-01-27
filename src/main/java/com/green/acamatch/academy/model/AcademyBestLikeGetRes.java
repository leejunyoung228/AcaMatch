package com.green.acamatch.academy.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcademyBestLikeGetRes {
    private long acaId;
    private int likeCount;
    private double starCount;
    private int reviewCount;
    private String pic;
}

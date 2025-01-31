package com.green.acamatch.academy.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AcademyBestLikeGetRes {
    private long acaId;
    private String acaName;
    private int likeCount;
    private double starAvg;
    private int reviewCount;
    private String acaPic;
    private String tagIds;
}

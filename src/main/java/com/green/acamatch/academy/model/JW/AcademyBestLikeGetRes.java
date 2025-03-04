package com.green.acamatch.academy.model.JW;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AcademyBestLikeGetRes {
    private long acaId;
    private String acaName;
    private int likeCount;
    private double starAvg;
    private int reviewCount;
    private String acaPic;
    private String tagIds;
    private String tagNames;
    private int academyLikeCount;
}

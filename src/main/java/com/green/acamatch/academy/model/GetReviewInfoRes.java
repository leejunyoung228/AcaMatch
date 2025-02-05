package com.green.acamatch.academy.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetReviewInfoRes {
    private long joinClassId;
    private String comment;
    private double star;
    private String createdAt;
    private int reviewCount;
    private double starAvg;
    private long userId;
    private String userPic;
    private String className;
}

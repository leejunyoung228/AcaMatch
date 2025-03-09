package com.green.acamatch.academy.model.HB;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralReviewDto {
    private long reviewId;
    private String reviewComment;
    private int reviewStar;
    private String reviewCreatedAt;
    private String reviewUpdatedAt;
    private long reviewUserId;
    private String reviewUserNickName;
    private String reviewClassName;
    private long joinClassId;
    private int banReview;
}
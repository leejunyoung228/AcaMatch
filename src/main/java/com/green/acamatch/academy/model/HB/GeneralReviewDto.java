package com.green.acamatch.academy.model.HB;

import io.swagger.v3.oas.annotations.media.Schema;
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
    private Long reviewUserId;
    private String reviewUserNickName;
    private String reviewClassName;
    private long joinClassId;
    private int banReview;
    private String writerPic;
    private long classId;
    private int totalGeneralReviewCount;
}
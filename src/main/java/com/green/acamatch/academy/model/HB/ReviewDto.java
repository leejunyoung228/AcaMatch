package com.green.acamatch.academy.model.HB;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReviewDto {
    private String comment;
    private double star;
    private long reviewId;
    private String createdAt;
    private String updatedAt;
    private long userId;
    private String nickName;
    private String className;
    private int banReview;
    private String roleType;
    private long joinClassId;
    private List<ReviewPicDto> reviewPics;
}


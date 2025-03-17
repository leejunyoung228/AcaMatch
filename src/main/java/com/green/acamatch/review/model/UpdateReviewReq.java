package com.green.acamatch.review.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateReviewReq {
    private long reviewId;
    private String comment;
    private Integer star;
}

package com.green.acamatch.review.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ReviewPostReq {
    private long joinClassId;
    private long userId;
    private String comment;
    private int star;
    private int banReview;
    @JsonIgnore
    private List<String> pics;
}

package com.green.acamatch.reports.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetReviewListRes {
    private String userPic;
    private String name;
    private String email;
    private String comment;
    private String reportsType;
    private String updatedAt;
    private String exposureEndDate;
    private int processingStatus;
    private int reportsCount;
    private int totalCount;
    private String actionType;
}

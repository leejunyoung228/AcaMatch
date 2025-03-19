package com.green.acamatch.reports.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserActionTypeRes {
    private String updatedAt;
    private String exposureEndDate;
    private String reportsType;
    private String actionType;
    private long reportId;
    private long userId;
    private String name;
}

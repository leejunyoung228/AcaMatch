package com.green.acamatch.reports.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAcademyListRes {
    private long acaId;
    private String acaName;
    private String acaPic;
    private String email;
    private String reportsType;
    private String updatedAt;
    private String exposureEndDate;
    private int totalCount;
    private int reportsCount;
    private int processingStatus;
    private String actionType;
}

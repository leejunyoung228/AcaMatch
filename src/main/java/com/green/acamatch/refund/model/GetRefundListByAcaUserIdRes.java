package com.green.acamatch.refund.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetRefundListByAcaUserIdRes {
    private long refundId;
    private String createdAt;
    private String updatedAt;
    private long costId;
    private String refundComment;
    private int refundStatus;
}

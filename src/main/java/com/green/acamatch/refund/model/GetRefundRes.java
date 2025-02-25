package com.green.acamatch.refund.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetRefundRes {
    private long refundId;
    private long costId;
    private String refundComment;
    private int refundStatus;
    private String createdAt;
    private String updatedAt;
}

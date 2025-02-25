package com.green.acamatch.refund.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRefundReq {
    private long costId;
    private String refundComment;
}

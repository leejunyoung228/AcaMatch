package com.green.acamatch.academyCost.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoCancelReq {
    private String tid;
    private String refundComment;
    private long costId;
}

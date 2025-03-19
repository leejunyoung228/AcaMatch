package com.green.acamatch.academyCost.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAcademyCostListByUserRes {
    private long acaId;
    private String acaName;
    private String classOrBookName;
    private String acaPic;
    private String createdAt;
    private int price;
    private int costStatus;
    private String name;
    private long costId;
    private String tId;
    private int refundStatus;
}

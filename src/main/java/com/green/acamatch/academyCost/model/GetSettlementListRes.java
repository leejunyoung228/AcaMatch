package com.green.acamatch.academyCost.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetSettlementListRes {
    private String acaName;
    private String address;
    private String acaPic;
    private Long costId;
    private Integer price;
    private Integer status;
    private String updatedAt;
    private Long acaId;
}

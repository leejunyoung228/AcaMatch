package com.green.acamatch.academyCost.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetProductInfoRes {
    private long productId;
    private String productName;
    private int productPrice;
    private Long bookId;
    private Long classId;
}

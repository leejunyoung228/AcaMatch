package com.green.acamatch.manager.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAcademyCostByUserIdReq {
    private String month;
    private Long userId;
}

package com.green.acamatch.academy.premium.model;

import com.green.acamatch.config.model.Paging;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PremiumGetPagingReq extends Paging {
    public PremiumGetPagingReq(Integer page, Integer size) {
        super(page, size);
    }
}

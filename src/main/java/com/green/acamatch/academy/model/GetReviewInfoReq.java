package com.green.acamatch.academy.model;

import com.green.acamatch.config.model.Paging;
import lombok.Getter;

@Getter
public class GetReviewInfoReq extends Paging {
    private long acaId;


    public GetReviewInfoReq(Integer page, Integer size,long acaId) {
        super(page, size);
        this.acaId = acaId;
    }
}

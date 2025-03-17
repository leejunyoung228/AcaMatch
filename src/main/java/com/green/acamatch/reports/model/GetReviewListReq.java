package com.green.acamatch.reports.model;

import com.green.acamatch.config.model.Paging;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetReviewListReq extends Paging {
    public GetReviewListReq(Integer page, Integer size) {
        super(page, size);
    }
}

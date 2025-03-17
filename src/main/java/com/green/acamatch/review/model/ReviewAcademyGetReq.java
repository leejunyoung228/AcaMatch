package com.green.acamatch.review.model;

import com.green.acamatch.config.model.Paging;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewAcademyGetReq extends Paging {
    private Long acaId;

    public ReviewAcademyGetReq(Integer page, Integer size) {
        super(page, size);
    }
}

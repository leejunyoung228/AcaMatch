package com.green.acamatch.review.model;

import com.green.acamatch.config.model.Paging;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ReviewAcademyAllGetReq extends Paging {
    private Long acaId;

    public ReviewAcademyAllGetReq(Integer page, Integer size) {
        super(page, size);
    }
}

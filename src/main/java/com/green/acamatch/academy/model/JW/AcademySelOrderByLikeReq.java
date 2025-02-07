package com.green.acamatch.academy.model.JW;

import com.green.acamatch.config.model.Paging;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class AcademySelOrderByLikeReq extends Paging {
    public AcademySelOrderByLikeReq(Integer page, Integer size) {
        super(page, size);
    }
}

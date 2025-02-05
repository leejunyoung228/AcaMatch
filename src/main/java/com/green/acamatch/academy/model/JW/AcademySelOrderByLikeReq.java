package com.green.acamatch.academy.model.JW;

import com.green.acamatch.config.model.Paging;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcademySelOrderByLikeReq extends Paging {
    public AcademySelOrderByLikeReq(Integer page, Integer size) {
        super(page, size);
    }
}

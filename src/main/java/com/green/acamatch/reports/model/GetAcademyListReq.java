package com.green.acamatch.reports.model;

import com.green.acamatch.config.model.Paging;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAcademyListReq extends Paging {

    public GetAcademyListReq(Integer page, Integer size) {
        super(page, size);
    }
}

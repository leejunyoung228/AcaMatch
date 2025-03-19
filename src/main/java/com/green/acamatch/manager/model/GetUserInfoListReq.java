package com.green.acamatch.manager.model;

import com.green.acamatch.config.model.Paging;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserInfoListReq extends Paging {
    private long userId;
    private int certification;
    private String name;

    public GetUserInfoListReq(Integer page, Integer size) {
        super(page, size);
    }
}

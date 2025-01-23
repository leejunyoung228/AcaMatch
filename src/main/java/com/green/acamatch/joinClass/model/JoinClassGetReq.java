package com.green.acamatch.joinClass.model;

import com.green.acamatch.config.model.Paging;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinClassGetReq extends Paging {
    private long userId;

    public JoinClassGetReq(Integer page, Integer size, long userId) {
        super(page, size);
        this.userId = userId;
    }
}
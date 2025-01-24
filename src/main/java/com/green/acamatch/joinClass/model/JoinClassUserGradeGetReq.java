package com.green.acamatch.joinClass.model;

import com.green.acamatch.config.model.Paging;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinClassUserGradeGetReq extends Paging {
    private long acaId;

    public JoinClassUserGradeGetReq(Integer page, Integer size, long acaId) {
        super(page, size);
        this.acaId = acaId;
    }
}
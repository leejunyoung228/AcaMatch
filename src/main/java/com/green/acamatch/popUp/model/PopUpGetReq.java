package com.green.acamatch.popUp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.acamatch.config.model.Paging;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PopUpGetReq extends Paging {
    @JsonIgnore
    private long popUpId;

    public PopUpGetReq(Integer page, Integer size) {
        super(page, size);
    }
}
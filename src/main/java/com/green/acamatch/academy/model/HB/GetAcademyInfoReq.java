package com.green.acamatch.academy.model.HB;

import com.green.acamatch.config.model.Paging;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAcademyInfoReq extends Paging {
    private String acaName;
    private String className;
    private String examName;
    private Integer acaAgree;

    public GetAcademyInfoReq(Integer page, Integer size) {
        super(page, size);
    }
}

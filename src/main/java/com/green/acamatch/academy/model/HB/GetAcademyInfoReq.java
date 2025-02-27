package com.green.acamatch.academy.model.HB;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAcademyInfoReq {
    private String acaName;
    private String className;
    private String examName;
    private Integer acaAgree;
}

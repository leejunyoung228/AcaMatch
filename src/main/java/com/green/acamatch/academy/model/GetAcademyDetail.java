package com.green.acamatch.academy.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAcademyDetail {
    private long acaId;
    private String acaPhone;
    private String acaName;
    private String accPic;
    private String comment;
    private int teacherNum;
    private String openTime;
    private String closeTime;
    private String address;
}

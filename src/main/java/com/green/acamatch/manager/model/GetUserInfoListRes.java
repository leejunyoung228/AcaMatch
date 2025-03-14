package com.green.acamatch.manager.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserInfoListRes {
    private String name;
    private String userPic;
    private String email;
    private String registrationDate;
    private String acaName;
    private String className;
    private String phone;
    private int certification;
}

package com.green.acamatch.teacher.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TeacherInfoGetRes {
    private long userId;
    private long classId;
    private long acaId;
    private String userPic;
    private String name;
    private String phone;
    private String nickName;
    private String email;
    private String acaName;
    private String className;
}
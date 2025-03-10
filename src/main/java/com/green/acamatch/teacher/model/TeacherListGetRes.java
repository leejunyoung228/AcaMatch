package com.green.acamatch.teacher.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TeacherListGetRes {
    private long classId;
    private String className;
    private long userId;
    private String userPic;
    private String name;
    private String email;
    private String birth;
    private String phone;
    private String teacherComment;
    private int teacherAgree;
}

package com.green.acamatch.grade.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GradeUserAndParentGetRes {
    private long userId;
    private String userPic;
    private String studentName;
    private String studentPhoneNumber;
    private Long parentId;
    private String parentName;
    private String parentPhoneNumber;
    private String email;
    private long joinClassId;
    private String birth;
}

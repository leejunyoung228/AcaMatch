package com.green.acamatch.teacher.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TeacherAgreePutReq {
    private long classId;
    private long userId;
    private int teacherAgree;
}
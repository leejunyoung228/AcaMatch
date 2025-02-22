package com.green.acamatch.entity.myenum;

public enum UserRole {
    ADMIN,
    STUDENT,
    PARENT,
    ACADEMY,
    TEACHER,
    NOT_SELECTED;

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public boolean isTeacher() {
        return this == TEACHER;
    }

    public boolean isAcademy() {
        return this == ACADEMY;
    }
}

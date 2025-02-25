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

    // 관리자(ADMIN), 선생(TEACHER), 학원(Academy)만 가능
    public boolean isAdminOrTeacherOrAcademy() {
        return isAdmin() || isTeacher() || isAcademy();
    }
}

package com.green.acamatch.user.model;

import com.green.acamatch.entity.myenum.UserRole;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface UserReportProjection {
    Long getUserId();
    UserRole getUserRole();
    String getName();
    String getEmail();
    String getPhone();
    LocalDate getBirth();
    String getNickName();
    String getUserPic();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    Long getReportsCount();
}
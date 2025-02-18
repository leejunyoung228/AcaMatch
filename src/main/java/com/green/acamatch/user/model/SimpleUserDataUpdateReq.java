package com.green.acamatch.user.model;

import com.green.acamatch.entity.myenum.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SimpleUserDataUpdateReq {
    private Long userId;
    private UserRole userRole;
    private String name;
    private String phone;
    private LocalDate birth;
    private String nickName;
    private String upw;
}

package com.green.acamatch.user.model;

import com.green.acamatch.entity.myenum.UserRole;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SimpleUserDataUpdateReq {
    @NotEmpty
    private Long userId;
    @NotEmpty
    private UserRole userRole;
    @NotEmpty
    private String name;
    @NotEmpty
    private String phone;
    @NotEmpty
    private LocalDate birth;
    @NotEmpty
    private String nickName;
    @NotEmpty
    private String upw;
}

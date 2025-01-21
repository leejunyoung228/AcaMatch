package com.green.acamatch.user.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class UserUpdateReq {
    private String name;
    @Pattern(regexp = "^(01[0-9])-\\d{3,4}-\\d{4}$", message = "Invalid phone number format. Example: 010-1234-5678")
    private String phone;
    private LocalDate birth;
    @Min(3)
    private String nickName;
}

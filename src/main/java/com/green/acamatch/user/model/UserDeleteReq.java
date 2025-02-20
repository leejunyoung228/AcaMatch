package com.green.acamatch.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDeleteReq {
    @Schema(description = "대소문자 특수문자 숫자 포함 8자 이상 20자 미만")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=\\S+$).{8,20}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    private String pw;
}

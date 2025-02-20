package com.green.acamatch.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class UserSignInReq {
    @Email
    @NotNull
    private String email;
    @Schema(description = "대소문자 특수문자 숫자 포함 8자 이상 20자 미만")
    @NotEmpty(message = "Password cannot be empty.")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\[\\]{}|;:,.<>?])(?=\\S+$).{8,20}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    private String upw;
}

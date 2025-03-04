package com.green.acamatch.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class UserUpdateReq {
    private String name;
    @Pattern(regexp = "^(01[0-9])-\\d{3,4}-\\d{4}$", message = "Invalid phone number format. Example: 010-1234-5678")
    private String phone;
    @NotEmpty
    @Schema(description = "대소문자 특수문자 숫자 포함 8자 이상 20자 미만")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\[\\]{}|;:,.<>?])(?=\\S+$).{8,20}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    private String currentPw;
    @Schema(description = "대소문자 특수문자 숫자 포함 8자 이상 20자 미만")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\[\\]{}|;:,.<>?])(?=\\S+$).{8,20}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    private String newPw;
    private LocalDate birth;
    @Size(min = 3)
    private String nickName;

    private String userPic;
}

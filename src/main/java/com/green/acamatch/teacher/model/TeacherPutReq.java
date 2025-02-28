package com.green.acamatch.teacher.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class TeacherPutReq {
    @Email
    @NotEmpty
    private String email;

    @Schema(title = "현재 비밀번호")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\[\\]{}|;:,.<>?])(?=\\S+$).{8,20}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    private String currentPw;

    @NotEmpty
    @Schema(title = "신규 비밀번호", description = "대소문자 특수문자 숫자 포함 8자 이상 20자 미만")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\[\\]{}|;:,.<>?])(?=\\S+$).{8,20}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    private String newPw;

    @Schema(title = "선생님 이름", description = "홍길동")
    private String name;

    @Schema(title = "닉네임")
    @Size(min = 3)
    private String nickName;

    @Schema(title = "휴대폰 번호", description = "010-1234-1234")
    @Pattern(regexp = "^(01[0-9])-\\d{3,4}-\\d{4}$", message = "Invalid phone number format. Example: 010-1234-5678")
    private String phone;

    @Schema(title = "생년월일", description = "2025-02-28")
    private LocalDate birth;

    @Schema(title = "선생님 사진")
    private String userPic;

    @Schema(title = "선생님 소개")
    private String teacherComment;
}

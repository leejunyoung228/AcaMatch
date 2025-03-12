package com.green.acamatch.acaClass.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AcaClassUserDto {
    @Schema(title = "유저 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long userId;
    @Schema(title = "회원 사진")
    private String userPic;
    @Schema(title = "회원 이름")
    private String name;
    @Schema(title = "회원 휴대폰번호")
    private String phone;
    @Schema(title = "회원 생년월일")
    private String birth;

    private long joinClassId;
}
package com.green.acamatch.manager.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherRegisterReq {

    @Schema(title = "학원 ID", example = "3")
    private Long acaId;

    @Schema(title = "사용자 ID", example = "5")
    private Long userId;

    @Schema(title = "선생님 소개", example = "국어 전문가입니다.")
    private String teacherComment;

    @Schema(title = "선생님 동의 여부 (0=미동의, 1=동의)", example = "1")
    private int teacherAgree;
}

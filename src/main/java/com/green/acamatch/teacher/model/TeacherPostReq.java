package com.green.acamatch.teacher.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TeacherPostReq {
    @JsonIgnore
    private long acaId;
    @Schema(title = "강좌 pk", example = "1")
    private long classId;
    @Schema(title = "사용자 pk", example = "1")
    private long userId;
    @Schema(title = "선생님 소개", example = "국어 전문가입니다.")
    private String teacherComment;
    @Schema(title = "선생님 동의 여부 (0=미동의, 1=동의)", example = "0")
    private int teacherAgree;
    @Schema(title = "선생님 퇴사 여부 (0=퇴사, 1=퇴사x", example = "1")
    private int isActive;
}
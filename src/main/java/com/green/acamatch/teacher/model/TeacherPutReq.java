package com.green.acamatch.teacher.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TeacherPutReq {
    @Schema(title = "학원 pk", example = "1")
    private long acaId;
    @Schema(title = "유저 pk", example = "1")
    private long userId;
    @Schema(title = "선생님 소개")
    private String teacherComment;
    @Schema(title = "선생님 동의 여부 (0=미동의, 1=동의)", example = "0")
    private int teacherAgree;
}
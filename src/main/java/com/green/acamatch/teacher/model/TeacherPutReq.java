package com.green.acamatch.teacher.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TeacherPutReq {
    @JsonIgnore
    private long acaId;
    @Schema(title = "강좌 pk", example = "1")
    private long classId;
    @Schema(title = "유저 pk", example = "1")
    private long userId;
    @Schema(title = "선생님 소개")
    private String teacherComment;
}
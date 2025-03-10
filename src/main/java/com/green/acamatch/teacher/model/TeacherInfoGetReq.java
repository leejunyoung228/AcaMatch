package com.green.acamatch.teacher.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TeacherInfoGetReq {
    @Schema(title = "선생님 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long userId;
}
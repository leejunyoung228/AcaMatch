package com.green.acamatch.academy.model.JW;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcademyDeleteReq {
    @Schema(description = "삭제할 학원정보 PK", example = "1")
    private long acaId;
    @Schema(description = "로그인한 사용자 PK", example = "1")
    private long userId;
}

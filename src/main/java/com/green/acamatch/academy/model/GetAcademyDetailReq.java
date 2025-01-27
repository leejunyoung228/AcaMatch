package com.green.acamatch.academy.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAcademyDetailReq {
    @Schema(title = "로그인한 유저 PK", example = "2119")
    private long signedUserId;

    @Schema(title = "학원 PK", example = "324")
    private long acaId;
}

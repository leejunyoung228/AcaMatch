package com.green.acamatch.academy.model.HB;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAcademyListByUserIdReq {
    @Schema(title = "로그인한 유저 PK", example = "2019")
    private long signedUserId;
}

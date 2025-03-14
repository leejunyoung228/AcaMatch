package com.green.acamatch.academy.model.HB;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAcademyListByUserIdReq {
    @Schema(title = "로그인한 유저 PK", example = "2019", description = "학원 관계자 user PK")
    private long signedUserId;

    @Schema(title = "학원 승인 여부", description = "학원 승인 여부")
    private int acaAgree;

    @Schema(title = "학원 이름", description = "학원 이름")
    private String acaName;
}

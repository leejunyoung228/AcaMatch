package com.green.acamatch.academy.model.HB;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAcademyListByUserIdRes {
    @Schema(title = "학원 PK", example = "2019, 2020")
    private long acaId;

    @Schema(title = "학원 이름", example = "중심미술학원, 한별미술학원")
    private String acaName;

    @Schema(title = "학원 등록 날짜", example = "2025-01-24")
    private String createdAt;

    private int acaAgree;

}

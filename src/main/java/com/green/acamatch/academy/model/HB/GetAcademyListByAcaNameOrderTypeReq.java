package com.green.acamatch.academy.model.HB;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAcademyListByAcaNameOrderTypeReq {
    @Schema(title = "학원 이름")
    private String acaName;

    @Schema(title = "주문 타입", description = "0이면 학원, 1이면 교재")
    private int orderType;

    @Schema(title = "주문자 이름")
    private String userName;

    @Schema(title = "시작일", example = "2025-02-01")
    private String startDate;

    @Schema(title = "종료일", example = "2025-03-31")
    private String endDate;
}

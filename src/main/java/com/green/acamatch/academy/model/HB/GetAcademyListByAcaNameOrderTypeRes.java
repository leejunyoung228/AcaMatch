package com.green.acamatch.academy.model.HB;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAcademyListByAcaNameOrderTypeRes {
    @Schema(title = "학원 이름")
    private String acaName;

    @Schema(title = "학원 소개")
    private String comment;

    @Schema(title = "학원 사진")
    private String acaPic;

    @Schema(title = "결제 날짜")
    private String createdAt;

    @Schema(title = "결제 금액")
    private int price;

    @Schema(title = "주문자 이름")
    private String name;

    @Schema(title = "결제 상태", description = "0이면 결제 대기, 1이면 결제 완료")
    private int costStatus;
}

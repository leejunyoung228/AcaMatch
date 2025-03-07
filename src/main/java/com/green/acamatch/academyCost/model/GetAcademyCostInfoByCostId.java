package com.green.acamatch.academyCost.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAcademyCostInfoByCostId {
    @Schema(title = "결제 PK", example = "4")
    private long costId;

    @Schema(title = "구매 수량", example = "1")
    private int amount;

    @Schema(title = "결제 상태", description = "0이면 결제 대기, 1이면 결제 취소, 2이면 결제 완료")
    private int costStatus;

    @Schema(title = "수수료", description = "결제 금액의 1%")
    private int fee;

    @Schema(title = "orderType", description = "0이면 학원/수업 결제, 1이면 교재 결제, 2이면 프리미엄 학원 결제")
    private int orderType;

    @Schema(title = "price", description = "결제 금액")
    private int price;

    @Schema(title = "정산 상태", description = "0이면 정산 X, 1이면 정산 O")
    private int status;

    @Schema(title = "학원 PK", description = "프리미엄 학원 결제한 학원의 PK, 프리미엄 결제가 아니면 0으로 나올겁니다.")
    private long accId;

    @Schema(title = "결제 날짜")
    private String createdAt;

    @Schema(title = "상품 PK")
    private long productId;

    @Schema(title = "정산 날짜")
    private String updatedAt;

    @Schema(title = "구매한 사람")
    private String name;

    @Schema(title = "결제 시에 발행된 tId")
    private String tId;

    @Schema(title = "결제 시에 발행된 partnerOrderId")
    private String partnerOrderId;

    @Schema(title = "상품 이름")
    private String productName;
}

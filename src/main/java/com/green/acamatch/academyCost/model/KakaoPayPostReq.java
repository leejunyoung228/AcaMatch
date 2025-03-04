package com.green.acamatch.academyCost.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KakaoPayPostReq {
    @Schema(title = "상품 목록", description = "상품 ID와 수량 리스트")
    private List<ProductRequest> products;

    @Schema(title = "유저 PK", example = "1")
    private int userId;

    @Schema(title = "어떤 수업 듣고 있는 학생인지", example = "0")
    private long joinClassId;

    @Schema(title = "학원 PK", example = "1", description = "프리미엄 학원 결제할 때만 입력")
    private Long acaId;
}



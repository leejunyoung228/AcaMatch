package com.green.acamatch.academyCost.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoPayPostReq {
    @Schema(title = "주문번호", example = "1")
    private Long productId;

    @Schema(title = "유저 PK", example = "1")
    private int userId;

    @Schema(title = "수량", example = "1")
    private int quantity;

    @Schema(title = "어떤 수업 듣고 있는 학생인지", example = "0")
    private long joinClassId;
}

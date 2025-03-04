package com.green.acamatch.academyCost.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    @Schema(title = "상품 ID", example = "2")
    private Long productId;

    @Schema(title = "수량", example = "2")
    private int quantity;
}

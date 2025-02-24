package com.green.acamatch.academy.premium.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PremiumDeleteReq {
    @Schema(title = "프리미엄 삭제할 학원pk")
    private Long acaId;
}

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
public class PremiumUpdateReq {
    @Schema(title = "프리미엄 승인여부", example = "1", description = "승인되면 preCheck = 1 보내주세요.")
    private Long acaId;
    private Integer preCheck;
}

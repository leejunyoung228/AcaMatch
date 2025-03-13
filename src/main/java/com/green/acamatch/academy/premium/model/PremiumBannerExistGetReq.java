package com.green.acamatch.academy.premium.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PremiumBannerExistGetReq extends Paging {
    @Schema(title = "학원pk")
    private Long userId;
    public PremiumBannerExistGetReq(Integer page, Integer size) {
        super(page, size);
    }
}

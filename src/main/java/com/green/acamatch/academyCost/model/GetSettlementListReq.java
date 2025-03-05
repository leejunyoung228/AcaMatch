package com.green.acamatch.academyCost.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetSettlementListReq extends Paging{
    @Schema(title = "정산 처리 상태", description = "0이면 정산 X, 1이면 정산 O 아무것도 없어도 됩니다.",example = "0")
    private Integer status;

    @Schema(title = "지정한 년도", example = "2025")
    private String year;

    @Schema(title = "지정한 월", example = "03")
    private String month;

    public GetSettlementListReq(Integer page, Integer size) {
        super(page, size);
    }
}

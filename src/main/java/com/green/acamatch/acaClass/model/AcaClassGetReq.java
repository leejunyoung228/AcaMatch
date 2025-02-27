package com.green.acamatch.acaClass.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AcaClassGetReq extends Paging {
    @Schema(title = "학원 PK", example = "1611", requiredMode = Schema.RequiredMode.REQUIRED)
    private long acaId;
    @Schema(title = "검색어", example = "과학 창의 반")
    private String keyword;

    public AcaClassGetReq(Integer page, Integer size, long acaId, String keyword) {
        super(page, size);
        this.acaId = acaId;
        this.keyword = keyword;
    }
}

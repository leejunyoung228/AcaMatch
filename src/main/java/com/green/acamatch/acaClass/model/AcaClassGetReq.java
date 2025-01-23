package com.green.acamatch.acaClass.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AcaClassGetReq extends Paging {
    @Schema(title = "학원 PK", example = "324", requiredMode = Schema.RequiredMode.REQUIRED)
    private long acaId;

    public AcaClassGetReq(Integer page, Integer size, long acaId) {
        super(page, size);
        this.acaId = acaId;
    }
}
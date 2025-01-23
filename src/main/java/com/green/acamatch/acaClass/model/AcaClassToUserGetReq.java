package com.green.acamatch.acaClass.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AcaClassToUserGetReq extends Paging {
    @Schema(title = "사용자 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long userId;

    public AcaClassToUserGetReq(Integer page, Integer size, long userId) {
        super(page, size);
        this.userId = userId;
    }
}
package com.green.acamatch.acaClass.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AcaClassUserGetReq extends Paging {
    @Schema(title = "수업 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long classId;

    public AcaClassUserGetReq(Integer page, Integer size, long classId) {
        super(page, size);
        this.classId = classId;
    }
}
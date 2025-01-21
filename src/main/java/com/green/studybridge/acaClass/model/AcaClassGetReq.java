package com.green.studybridge.acaClass.model;

import com.green.studybridge.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AcaClassGetReq extends Paging {
    @Schema(title = "학원 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long acaId;


    public AcaClassGetReq(Integer page, Integer size, long acaId) {
        super(page, size);
        this.acaId = acaId;
    }
}
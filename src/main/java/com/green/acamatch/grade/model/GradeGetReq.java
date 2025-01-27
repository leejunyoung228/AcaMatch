package com.green.acamatch.grade.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GradeGetReq extends Paging {
    @Schema(title = "수강생 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long joinClassId;

    public GradeGetReq(Integer page, Integer size, long joinClassId) {
        super(page, size);
        this.joinClassId = joinClassId;
    }
}
package com.green.acamatch.grade.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GradeStatusGetReq extends Paging {
    @Schema(title = "학원 PK", example = "1611", requiredMode = Schema.RequiredMode.REQUIRED)
    private long acaId;
    @Schema(title = "수업 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long classId;

    public GradeStatusGetReq(Integer page, Integer size, long acaId, long classId) {
        super(page, size);
        this.acaId = acaId;
        this.classId = classId;
    }
}

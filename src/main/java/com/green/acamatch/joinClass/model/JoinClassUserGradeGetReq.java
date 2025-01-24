package com.green.acamatch.joinClass.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinClassUserGradeGetReq extends Paging {
    @Schema(title = "학원 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long acaId;
    @Schema(title = "joinClass PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long joinClassId;
    @Schema(title = "시험 회차 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long subjectId;

    public JoinClassUserGradeGetReq(Integer page, Integer size, long acaId, long joinClassId, long subjectId) {
        super(page, size);
        this.acaId = acaId;
        this.joinClassId = joinClassId;
        this.subjectId = subjectId;
    }
}
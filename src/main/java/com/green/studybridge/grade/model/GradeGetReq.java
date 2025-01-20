package com.green.studybridge.grade.model;

import com.green.studybridge.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GradeGetReq extends Paging {
    @Schema(title = "수업 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long classId;
    @Schema(title = "사용자 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long userId;

    public GradeGetReq(Integer page, Integer size, long classId) {
        super(page, size);
        this.classId = classId;
    }
}

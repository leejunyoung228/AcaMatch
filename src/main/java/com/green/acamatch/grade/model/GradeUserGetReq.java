package com.green.acamatch.grade.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GradeUserGetReq extends Paging {
    @Schema(title = "시험 회차 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long examId;

    public GradeUserGetReq(Integer page, Integer size, long examId) {
        super(page, size);
        this.examId = examId;
    }
}
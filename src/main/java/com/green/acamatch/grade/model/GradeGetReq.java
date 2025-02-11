package com.green.acamatch.grade.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GradeGetReq extends Paging {
    @Schema(title = "유저 pk", example = "1" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private long userId;
    @Schema(title = "강좌 pk", example = "1" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private long classId;

    public GradeGetReq(Integer page, Integer size, long userId, long classId) {
        super(page, size);
        this.userId = userId;
        this.classId = classId;
    }
}
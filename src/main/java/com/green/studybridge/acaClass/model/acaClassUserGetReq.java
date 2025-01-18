package com.green.studybridge.acaClass.model;

import com.green.studybridge.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class acaClassUserGetReq extends Paging {
    @Schema(title = "사용자 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long userId;

    public acaClassUserGetReq(Integer page, Integer size, long userId) {
        super(page, size);
        this.userId = userId;
    }
}
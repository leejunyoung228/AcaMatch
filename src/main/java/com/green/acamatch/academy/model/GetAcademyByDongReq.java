package com.green.acamatch.academy.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAcademyByDongReq extends Paging {
    @Schema(title = "동 PK", example = "778", requiredMode = Schema.RequiredMode.REQUIRED)
    private long dongId;

    public GetAcademyByDongReq(Integer page, Integer size, long dongId) {
        super(page, size);
        this.dongId = dongId;
    }

}

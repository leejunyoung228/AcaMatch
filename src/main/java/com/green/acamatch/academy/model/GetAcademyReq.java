package com.green.acamatch.academy.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class GetAcademyReq extends Paging {
    @Schema(title = "동 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long dongId;

    @Schema(title = "시/군/구 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long streetId;

    @Schema(title = "도시 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long cityId;

    @Schema(title = "검색할 태그 이름", example = "국어", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tagName;

    public GetAcademyReq(Integer size, Integer page, long dongId, long streetId, long cityId, String tagName) {
        super(size, page);
        this.dongId = dongId;
        this.streetId = streetId;
        this.cityId = cityId;
        this.tagName = tagName;
    }
}

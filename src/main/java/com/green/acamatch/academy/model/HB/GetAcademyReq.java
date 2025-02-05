package com.green.acamatch.academy.model.HB;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class GetAcademyReq extends Paging {
    @Schema(title = "동 PK", example = "778", requiredMode = Schema.RequiredMode.REQUIRED)
    private long dongId;

    @Schema(title = "검색할 태그 이름", example = "영어", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tagName;

    @Schema(title = "태그 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long tagId;

    public GetAcademyReq(Integer page, Integer size, long dongId, String tagName, long tagId) {
        super(page, size);
        this.dongId = dongId;
        this.tagName = tagName;
        this.tagId = tagId;
    }
}

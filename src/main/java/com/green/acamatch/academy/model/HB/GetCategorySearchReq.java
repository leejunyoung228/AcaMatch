package com.green.acamatch.academy.model.HB;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GetCategorySearchReq extends Paging {
    @Schema(title = "동 PK", example = "778", requiredMode = Schema.RequiredMode.REQUIRED)
    private long dongId;

    @Schema(title = "검색할 카테고리 이름", example = "청소년", requiredMode = Schema.RequiredMode.REQUIRED)
    private String FirstCategoryName;

    @Schema(title = "검색할 카테고리 이름", example = "상급", requiredMode = Schema.RequiredMode.REQUIRED)
    private String SecondCategoryName;

    public GetCategorySearchReq(Integer page, Integer size, long dongId, String FirstCategoryName, String SecondCategoryName) {
        super(page, size);
        this.dongId = dongId;
        this.FirstCategoryName = FirstCategoryName;
        this.SecondCategoryName = SecondCategoryName;
    }
}

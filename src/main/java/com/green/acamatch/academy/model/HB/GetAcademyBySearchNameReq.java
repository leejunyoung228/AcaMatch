package com.green.acamatch.academy.model.HB;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAcademyBySearchNameReq extends Paging {
    @Schema(title = "동 PK", example = "778", requiredMode = Schema.RequiredMode.REQUIRED)
    private long dongId;

    @Schema(title = "검색어 이름", example = "영어", requiredMode = Schema.RequiredMode.REQUIRED)
    private String searchName;

    public GetAcademyBySearchNameReq(Integer page, Integer size, long dongId, String searchName) {
        super(page, size);
        this.dongId = dongId;
        this.searchName = searchName;
    }
}

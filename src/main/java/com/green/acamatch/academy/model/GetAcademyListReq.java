package com.green.acamatch.academy.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAcademyListReq extends Paging {
    @Schema(title = "동 PK", example = "778")
    private Integer dongId;

    @Schema(title = "검색어", example = "영어")
    private String searchName;

    @Schema(title = "태그명", example = "초등 영어")
    private String tagName;

    @Schema(title = "카테고리 이름", example = "청소년")
    private String FCName;

    @Schema(title = "카테고리 이름2", example = "상급")
    private String SCName;

    public GetAcademyListReq(Integer page, Integer size, Integer dongId, String searchName, String tagName, String FCName, String SCName) {
        super(page,size);
        this.dongId = dongId;
        this.searchName = searchName;
        this.tagName = tagName;
        this.FCName = FCName;
        this.SCName = SCName;
    }

}

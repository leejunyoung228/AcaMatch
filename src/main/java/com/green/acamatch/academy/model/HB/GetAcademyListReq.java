package com.green.acamatch.academy.model.HB;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetAcademyListReq extends Paging {
    @Schema(title = "동 PK", example = "778")
    private Integer dongId;

    @Schema(title = "검색어", example = "영어")
    private String searchName;

    @Schema(title = "태그명", example = "초등 영어")
    private String tagName;

    @JsonIgnore
    private Long tagId;

    @Schema(title = "카테고리 PK", example = "")
    private List<Integer> categoryIds;

    @Schema(title = "프리미엄 학원 LIMIT", example = "3")
    private Integer premiumLimit;

    public GetAcademyListReq(Integer page, Integer size, Integer dongId, String searchName, String tagName, List<Integer> categoryIds, Integer premiumLimit) {
        super(page, size);
        this.dongId = dongId;
        this.searchName = searchName;
        this.tagName = tagName;
        this.categoryIds = categoryIds;
        this.premiumLimit = premiumLimit;
    }

}

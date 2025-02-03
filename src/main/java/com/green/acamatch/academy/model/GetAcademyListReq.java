package com.green.acamatch.academy.model;

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

    @Schema(title = "태그명", example = "1")
    private Integer tagId;

    @Schema(title = "카테고리 PK", example = "1, 2, 5, 6 한칸에 하나씩 넣으면 되빈다.")
    private List<Integer> categoryIds;

    public GetAcademyListReq(Integer page, Integer size, Integer dongId, String searchName, Integer tagId, List<Integer> categoryIds) {
        super(page, size);
        this.dongId = dongId;
        this.searchName = searchName;
        this.tagId = tagId;
        this.categoryIds = categoryIds;
    }

}

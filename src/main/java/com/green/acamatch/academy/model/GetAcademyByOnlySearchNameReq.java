package com.green.acamatch.academy.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAcademyByOnlySearchNameReq extends Paging {

    @Schema(title = "검색어 이름", example = "영어", requiredMode = Schema.RequiredMode.REQUIRED)
    private String searchName;

    public GetAcademyByOnlySearchNameReq(Integer page, Integer size, String searchName) {
        super(page, size);
        this.searchName = searchName;
    }
}

package com.green.acamatch.acaClass.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AcaClassGetReq extends Paging {
    @Schema(title = "학원 PK", example = "1")
    private Long acaId;
    @Schema(title = "검색어", example = "대구 ABC 상아탑 학원")
    private String keyword1;
    @Schema(title = "검색어", example = "과학 창의 반")
    private String keyword2;
    @Schema(title = "검색어", example = "ABC 학원 1차 모의고사")
    private String keyword3;

    public AcaClassGetReq(Integer page, Integer size, Long acaId, String keyword1, String keyword2, String keyword3) {
        super(page, size);
        this.acaId = acaId;
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
        this.keyword3 = keyword3;
    }
}

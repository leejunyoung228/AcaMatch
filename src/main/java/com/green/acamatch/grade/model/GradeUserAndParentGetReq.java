package com.green.acamatch.grade.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GradeUserAndParentGetReq extends Paging {
    @Schema(title = "학원 PK", example = "1")
    private long acaId;
    @Schema(title = "강좌 PK", example = "1")
    private long classId;
    @Schema(title = "검색어", example = "테스트학원")
    private String keyword1;
    @Schema(title = "검색어", example = "초등영어")
    private String keyword2;

    public GradeUserAndParentGetReq(Integer page, Integer size, long acaId, long classId, String keyword1, String keyword2) {
        super(page, size);
        this.acaId = acaId;
        this.classId = classId;
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
    }
}

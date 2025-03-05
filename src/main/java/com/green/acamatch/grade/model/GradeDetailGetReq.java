package com.green.acamatch.grade.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GradeDetailGetReq extends Paging {
//    @JsonIgnore
//    private long acaId;
//    @JsonIgnore
//    private long classId;
    @Schema(title = "시험 PK", example = "1")
    private Long examId;

    @Schema(title = "검색어", example = "테스트학원")
    private String keyword1;
    @Schema(title = "검색어", example = "초등 영어")
    private String keyword2;
    @Schema(title = "검색어", example = "1차 모의고사")
    private String keyword3;

    public GradeDetailGetReq(Integer page, Integer size, Long examId, String keyword1, String keyword2, String keyword3) {
        super(page, size);
        this.examId = examId;
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
        this.keyword3 = keyword3;
    }
}
package com.green.acamatch.academy.model.HB;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GetAcademyMediaReview {

    private Integer mediaStartIndx = 0;


    @Schema(title = "학원 PK", example = "1")
    private long acaId;

    @Schema(title = "페이지당 항목 수", example = "30")
    private Integer size;

    @JsonIgnore
    private Integer startIdx; // startIdx 추가

    public GetAcademyMediaReview() {
        // 기본 생성자
    }

    public GetAcademyMediaReview(Integer mediaStartIndx, Integer size, long acaId) {
        this.mediaStartIndx = mediaStartIndx;
        this.size = size;
        this.acaId = acaId;
        this.startIdx = (mediaStartIndx == null || mediaStartIndx < 0) ? 0 : mediaStartIndx; // startIdx를 mediaStartIndx로 설정
    }
}

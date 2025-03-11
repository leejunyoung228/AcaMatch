package com.green.acamatch.academy.model.HB;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAcademyGeneralReview {

    private Integer generalStartIndx = 0;

    @Schema(title = "학원 PK", example = "1")
    private long acaId;

    @Schema(title = "페이지당 항목 수", example = "30")
    private Integer size;

    @JsonIgnore
    private Integer startIdx; // startIdx 추가

    public GetAcademyGeneralReview() {
        // 기본 생성자
    }

    public GetAcademyGeneralReview(Integer generalStartIndx, Integer size, long acaId) {
        this.generalStartIndx = generalStartIndx;
        this.size = size;
        this.acaId = acaId;
        this.startIdx = (generalStartIndx == null || generalStartIndx < 0) ? 0 : generalStartIndx;
    }
}

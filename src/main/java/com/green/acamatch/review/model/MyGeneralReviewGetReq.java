package com.green.acamatch.review.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyGeneralReviewGetReq {

    @Schema(title = "유저 ID", description = "리뷰를 조회할 유저 ID", example = "1")
    private Long signedUserId;


    private Integer generalStartIndx = 0;



    @Schema(title = "페이지당 항목 수", example = "30")
    private Integer size;

    @JsonIgnore
    private Integer startIdx; // startIdx 추가

    public MyGeneralReviewGetReq() {
        // 기본 생성자
    }

    public MyGeneralReviewGetReq(Long signedUserId, Integer generalStartIndx, Integer size) {
        this.signedUserId = signedUserId;
        this.generalStartIndx = generalStartIndx;
        this.size = size;
        this.startIdx = (generalStartIndx == null || generalStartIndx < 0) ? 0 : generalStartIndx;
    }
}

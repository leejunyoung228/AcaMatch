package com.green.acamatch.review.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyMediaReviewGetReq {

    @Schema(title = "유저 ID", description = "리뷰를 조회할 유저 ID", example = "1")
    private Long signedUserId;


    private Integer mediaStartIndx = 0;



    @Schema(title = "페이지당 항목 수", example = "30")
    private Integer size;

    @JsonIgnore
    private Integer startIdx; // startIdx 추가

    public MyMediaReviewGetReq() {
        // 기본 생성자
    }

    public MyMediaReviewGetReq(Long signedUserId, Integer mediaStartIndx, Integer size) {
        this.signedUserId = signedUserId;
        this.mediaStartIndx = mediaStartIndx;
        this.size = size;
        this.startIdx = (mediaStartIndx == null || mediaStartIndx < 0) ? 0 : mediaStartIndx;
    }
}

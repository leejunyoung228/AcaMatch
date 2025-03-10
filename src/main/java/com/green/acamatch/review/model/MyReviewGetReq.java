package com.green.acamatch.review.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyReviewGetReq extends Paging {

    @Schema(title = "유저 ID", description = "리뷰를 조회할 유저 ID", example = "1")
    private Long userId;

    @Schema(title = "일반 리뷰 페이징 시작 인덱스", example = "0")
    private Integer generalStartIdx;

    @Schema(title = "미디어 포함 리뷰 페이징 시작 인덱스", example = "0")
    private Integer mediaStartIdx;

    public MyReviewGetReq(Integer page, Integer size, Long userId, Integer generalStartIdx, Integer mediaStartIdx) {
        super(page, size);
        this.userId = userId == null ? 0 : userId;
        this.generalStartIdx = generalStartIdx == null ? 0 : generalStartIdx;
        this.mediaStartIdx = mediaStartIdx == null ? 0 : mediaStartIdx;
    }
}

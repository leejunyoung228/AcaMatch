package com.green.acamatch.review.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "리뷰 등록 요청")
public class ReviewPostReq {
    @Schema(title = "수업 참여 ID", description = "수업 참여의 고유 ID", example = "2", required = true)
    private Long joinClassId;

    @Schema(title = "리뷰 내용", description = "작성할 리뷰 내용", example = "수업이 매우 유익했습니다!")
    private String comment;

    @Schema(title = "별점", description = "리뷰 별점", example = "5")
    private int star;
}
package com.green.acamatch.review.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Schema(description = "리뷰 수정 요청")
public class ReviewUpdateReq {

    @Schema(title = "유저 ID", description = "리뷰 삭제 요청을 한 유저의 ID", example = "1")
    private long userId;

    @Schema(title = "수업 참여 ID", description = "리뷰를 수정할 수업 참여 ID", example = "1")
    private Long joinClassId;

    @Schema(title = "리뷰 내용", description = "수정된 리뷰 내용", example = "수업이 매우 흥미로웠습니다!")
    private String comment;

    @Schema(title = "별점", description = "수정된 리뷰 별점", example = "4")
    private int star;

}
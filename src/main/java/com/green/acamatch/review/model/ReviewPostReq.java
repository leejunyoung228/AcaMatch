package com.green.acamatch.review.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Schema(description = "리뷰 등록 요청")
public class ReviewPostReq {

    @Schema(title = "수업 ID", description = "수업의 ID", example = "150", required = true)
    private Long classId;

    @Schema(title = "리뷰 내용", description = "작성할 리뷰 내용", example = "수업이 매우 유익했습니다!")
    private String comment;

    @Schema(title = "별점", description = "리뷰 별점", example = "5")
    private int star;

    @Schema(title = "유저 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long userId;
}
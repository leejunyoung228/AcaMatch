package com.green.acamatch.review.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ReviewPostReq {

    @Schema(title = "joinClass PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long joinClassId;
    @Schema(title = "유저 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long userId;
    @Schema(title = "리뷰 내용", example = "리뷰 작성")
    private String comment;
    @Schema(title = "별점", example = "1")
    private int star;
    @Schema(title = "금지된 리뷰 여부", example = "0")
    private int banReview;

    @JsonIgnore
    private List<String> pics;
}

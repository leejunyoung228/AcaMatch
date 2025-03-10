package com.green.acamatch.review.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Schema(description = "리뷰 삭제 요청")
public class ReviewDelReq {

    @Schema(title = "수업 ID", description = "수업의 ID", example = "1", required = true)
    private Long classId;

    @Schema(title = "유저 ID", description = "리뷰 삭제 요청을 한 유저의 ID", example = "1")
    private Long userId;

    @JsonIgnore
    private Long reviewId;


    @JsonIgnore
    private Long joinClassId;

    @JsonIgnore
    private Long acaId;
}
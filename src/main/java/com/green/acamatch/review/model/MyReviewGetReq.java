package com.green.acamatch.review.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyReviewGetReq extends Paging {

    @Schema(title = "유저 ID", description = "리뷰를 조회할 유저 ID", example = "1")
    private long userId;

    public MyReviewGetReq(Integer page, Integer size) {
        super(page, size);
    }
}

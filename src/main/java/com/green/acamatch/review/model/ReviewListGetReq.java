package com.green.acamatch.review.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewListGetReq extends Paging {
    @Schema(title = "학원 ID", description = "학원 고유 ID", example = "324")
    private long acaId;

    @Schema(title = "작성자 ID", description = "리뷰 작성자의 유저 ID", example = "1")
    private Long userId;

    public ReviewListGetReq(Integer page, Integer size) {
        super(page, size);
    }
}

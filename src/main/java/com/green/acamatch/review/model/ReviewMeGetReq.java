package com.green.acamatch.review.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ReviewMeGetReq extends Paging {
    @Schema(title = "유저 pk")
    private Long userId;

    public ReviewMeGetReq(Integer page, Integer size) {
        super(page, size);
    }

}

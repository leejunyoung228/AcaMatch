package com.green.acamatch.review.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.web.servlet.ServletComponentScan;

@Getter
@Setter
@ToString
public class ReviewMeDeleteReq {
    @Schema(title = "본인 유저 Pk")
    private Long userId;
    @Schema(title = "리뷰 pk")
    private Long reviewId;
}

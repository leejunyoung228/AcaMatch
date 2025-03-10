package com.green.acamatch.review.dto;

import com.green.acamatch.academy.model.HB.GeneralReviewDto;
import com.green.acamatch.academy.model.HB.MediaReviewDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReviewResponseDto {
    private List<GeneralReviewDto> generalReviews;
    private List<MediaReviewDto> mediaReviews;

    @Schema(title = "미디어 포함 리뷰 개수", example = "50")
    private Integer mediaReviewCount;

    @Schema(title = "일반 리뷰 개수", example = "50")
    private Integer generalReviewCount;

    @Schema(title = "모든 리뷰 개수", example = "100")
    private Integer reviewCount;

}

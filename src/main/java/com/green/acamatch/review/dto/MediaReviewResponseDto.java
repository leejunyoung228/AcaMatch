package com.green.acamatch.review.dto;

import com.green.acamatch.academy.model.HB.MediaReviewDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MediaReviewResponseDto {
    private List<MediaReviewDto> mediaReviews;
    private int totalMediaReviewCount;
}

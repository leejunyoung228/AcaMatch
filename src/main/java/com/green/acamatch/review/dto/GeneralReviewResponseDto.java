package com.green.acamatch.review.dto;

import com.green.acamatch.academy.model.HB.GeneralReviewDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GeneralReviewResponseDto {
    private List<GeneralReviewDto> generalReviews;
    private int totalGeneralReviewCount;
}
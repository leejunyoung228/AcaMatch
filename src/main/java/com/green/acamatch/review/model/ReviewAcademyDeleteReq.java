package com.green.acamatch.review.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ReviewAcademyDeleteReq {
    @Schema(title = "리뷰pk")
    private String reviewId;
}

package com.green.acamatch.academy.model.HB;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Data
public class MediaReviewDto {
    private int reviewId;
    private String reviewComment;
    private int reviewStar;
    private String reviewCreatedAt;
    private String reviewUpdatedAt;
    private int reviewUserId;
    private String reviewUserNickName;
    private String reviewClassName;
    private int joinClassId;
    private int banReview;
    private List<String> reviewPics;
    private String writerPic;
    private long classId;
}

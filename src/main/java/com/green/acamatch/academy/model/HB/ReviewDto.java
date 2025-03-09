package com.green.acamatch.academy.model.HB;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReviewDto {
    private String comment;
    private double star;
    private long reviewId;
    private String createdAt;
    private String updatedAt;
    private long userId;
    private String nickName;
    private String className;
    private int banReview;
    private Integer roleType; // Integer 타입으로 변경 (필요 시 ENUM으로 변경 가능)
    private long joinClassId;
    private long classId;

    // 기본적으로 빈 리스트로 초기화하여 NullPointerException 방지
    private List<ReviewPicDto> reviewPics = new ArrayList<>();

    // "general" 또는 "media" 값을 가질 수 있도록 기본값 설정
    private String reviewType;
}

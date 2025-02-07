package com.green.acamatch.academy.model.HB;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto {
    private Long reviewId;            // 리뷰 ID
    private String reviewComment;      // 리뷰 내용
    private Integer reviewStar;        // 별점
    private String reviewCreatedAt;    // 리뷰 작성일
    private Long reviewUserId;         // 리뷰 작성자 ID
    private String reviewUserNickName; // 리뷰 작성자 닉네임
    private String reviewClassName;    // 리뷰 대상 클래스 이름
    private Long reviewJoinClassId;
}
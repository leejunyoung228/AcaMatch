package com.green.acamatch.review.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto {

    @Schema(title = "수업 ID", description = "개설된 수업강좌 ID", example = "5")
    private Long classId;

    @Schema(title = "강좌 이름", description = "개설된 수업강좌 이름", example = "원어민 영어")
    private String className;

    @Schema(title = "작성자 ID", description = "리뷰 작성자의 유저 ID", example = "1")
    private Long userId;

    @Schema(title = "작성자 닉네임", description = "리뷰 작성자의 닉네임", example = "Student123")
    private String writerName;

    @Schema(title = "작성자 프로필 사진", description = "리뷰 작성자의 프로필 사진 파일명", example = "default_user_pic.jpg")
    private String writerPic;

    @Schema(title = "리뷰 내용", description = "리뷰 내용", example = "정말 좋은 수업이었습니다!")
    private String comment;

    @Schema(title = "별점", description = "리뷰 별점", example = "5")
    private int star;

    @Schema(title = "작성 날짜", description = "리뷰 작성 날짜", example = "2025-01-18")
    private String createdAt;

}
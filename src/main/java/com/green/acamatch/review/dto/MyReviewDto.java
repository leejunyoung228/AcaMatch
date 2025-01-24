package com.green.acamatch.review.dto;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyReviewDto extends Paging {

    @Schema(title = "수업 참여 ID", description = "리뷰가 연결된 수업 참여 ID", example = "5")
    private long joinClassId;  // 리뷰 ID

    @Schema(title = "리뷰 내용", description = "리뷰 내용", example = "정말 좋은 수업이었습니다!")
    private String comment;    // 리뷰 내용

    @Schema(title = "별점", description = "리뷰 별점", example = "5")
    private int star;          // 별점

    @Schema(title = "작성 날짜", description = "리뷰 작성 날짜", example = "2025-01-18")
    private String createdAt; // 작성 시간

    @Schema(title = "학원 ID", description = "학원 고유 ID", example = "101")
    private long acaId;        // 학원 ID

    @Schema(title = "학원 이름", description = "학원 이름", example = "이혜림음악학원")
    private String acaName; // 학원 이름

    @Schema(title = "작성자 프로필 사진", description = "리뷰 작성자의 프로필 사진 파일명", example = "default_user_pic.jpg")
    private String userPic;

    public MyReviewDto(Integer page, Integer size) {
        super(page, size);
    }
}
package com.green.acamatch.review.dto;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto  extends Paging {

    @Schema(title = "수업 참여 ID", description = "리뷰가 연결된 수업 참여 ID", example = "5")
    private Long joinClassId;

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

    public ReviewDto(Integer page, Integer size) {
        super(page, size);
    }
}
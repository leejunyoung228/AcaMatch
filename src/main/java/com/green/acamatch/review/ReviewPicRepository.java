package com.green.acamatch.review;

import com.green.acamatch.entity.review.Review;
import com.green.acamatch.entity.review.ReviewPic;
import com.green.acamatch.entity.review.ReviewPicIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewPicRepository extends JpaRepository<ReviewPic, ReviewPicIds> {

    // 특정 리뷰에 등록된 사진 전체 삭제
    void deleteByReview(Review review);


    @Query("SELECT rp.reviewPicIds.reviewPic FROM ReviewPic rp WHERE rp.reviewPicIds.reviewId = :reviewId")
    List<String> findFilePathsByReview(@Param("reviewId") Long reviewId);

    // 특정 리뷰 ID와 파일 경로로 파일 조회
    Optional<ReviewPic> findByReviewPicIds_ReviewIdAndReviewPicIds_ReviewPic(Long reviewId, String reviewPic);

    // 특정 리뷰의 사진 개수 조회 (최소 1개 보장)
    @Query("SELECT COUNT(rp) FROM ReviewPic rp WHERE rp.review.reviewId = :reviewId")
    long countByReviewPicIds_ReviewId(@Param("reviewId") Long reviewId);

    // 특정 리뷰 ID와 파일 경로로 삭제
    void deleteByReviewPicIds_ReviewIdAndReviewPicIds_ReviewPic(Long reviewId, String reviewPic);

}

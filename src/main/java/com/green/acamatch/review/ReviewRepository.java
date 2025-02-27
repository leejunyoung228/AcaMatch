package com.green.acamatch.review;

import com.green.acamatch.entity.review.Review;
import com.green.acamatch.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 특정 JoinClass + 특정 유저의 리뷰 존재 여부 확인 (중복 방지)
    Optional<Review> findByJoinClass_JoinClassIdAndUser_UserId(Long joinClassId, Long userId);


    // 특정 사용자가 작성한 리뷰 조회
    List<Review> findByUser_UserId(Long userId);

    // 특정 JoinClass에 속한 리뷰 조회 (수업별 리뷰 목록)
    List<Review> findByJoinClass_JoinClassId(Long joinClassId);

    // 리뷰 ID로 리뷰 조회
    Optional<Review> findByReviewId(Long reviewId);

    // 리뷰 작성자 조회
    @Query("SELECT r.user FROM Review r WHERE r.reviewId = :reviewId")
    User findUserByReview(@Param("reviewId") Long reviewId);
}

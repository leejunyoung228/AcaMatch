package com.green.acamatch.review;

import com.green.acamatch.entity.joinClass.JoinClass;
import com.green.acamatch.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByJoinClass_AcaClass_ClassIdAndUser_UserId(Long classId, Long userId);

    Integer countReviewsByJoinClass_AcaClass_ClassIdAndUser_UserId(Long classId, Long userId);

    List<Review> findByJoinClass_AcaClass_ClassIdAndUser_UserId(Long classId, Long userId);

    @Query("SELECT COUNT(r) > 0 FROM Review r WHERE r.reviewId = :reviewId AND r.joinClass.acaClass.classId = :classId")
    boolean existsByReviewIdAndClassId(@Param("reviewId") Long reviewId, @Param("classId") Long classId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Review r WHERE r.reviewId = :reviewId AND r.joinClass.acaClass.classId = :classId")
    int deleteByReviewIdAndClassId(@Param("reviewId") Long reviewId, @Param("classId") Long classId);

    boolean existsByJoinClass( @Param("joinClassId") JoinClass joinClassId);
}

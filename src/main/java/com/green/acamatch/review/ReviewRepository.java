package com.green.acamatch.review;

import com.green.acamatch.entity.review.Review;
import com.green.acamatch.entity.user.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByJoinClass_AcaClass_ClassIdAndUser_UserId(Long classId, Long userId);

    Integer countReviewsByJoinClass_AcaClass_ClassIdAndUser_UserId(Long classId, Long userId);

    List<Review> findByJoinClass_AcaClass_Academy_AcaIdAndUser_UserId(Long acaId, Long userId);
}

package com.green.acamatch.review;

import com.green.acamatch.entity.review.Review;
import com.green.acamatch.entity.review.ReviewPic;
import com.green.acamatch.entity.review.ReviewPicIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewPicRepository extends JpaRepository<ReviewPic, ReviewPicIds> {
    @Modifying
    @Query(" delete from ReviewPic a where a.review=:review ")
    int deleteReviewPicsByReviewId(Review review);

    @Query("select a.review from ReviewPic a where a.reviewPicIds=:reviewPicIds")
    Review findReviewIdsByReviewPicIds(ReviewPicIds reviewPicIds);
}

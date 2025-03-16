package com.green.acamatch.review;

import com.green.acamatch.entity.review.ReviewPic;
import com.green.acamatch.entity.review.ReviewPicIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewPicRepository extends JpaRepository<ReviewPic, ReviewPicIds> {
}

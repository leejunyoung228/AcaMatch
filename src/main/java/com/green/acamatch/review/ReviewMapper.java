
package com.green.acamatch.review;

import com.green.acamatch.academy.model.HB.GeneralReviewDto;
import com.green.acamatch.academy.model.HB.MediaReviewDto;

import com.green.acamatch.review.dto.ReviewDto;
import com.green.acamatch.review.model.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReviewMapper {

    List<ReviewAcademyAllGetRes> getAcademyReviewsAll(ReviewAcademyAllGetReq req);
    List<ReviewMeGetRes> getMeReviews(ReviewMeGetReq req);
    List<ReviewMeGetRes> getMeNoPicReviews(ReviewMeGetReq req);
    List<ReviewMeGetRes> getMePicReviews(ReviewMeGetReq req);

    int delAcademyReview(ReviewAcademyDeleteReq req);
    int delMeReview(ReviewMeDeleteReq req);
    List<ReviewAcademyGetRes> getAcademyPicReviews(ReviewAcademyGetReq req);
    List<ReviewAcademyGetRes> getAcademyNoPicReviews(ReviewAcademyGetReq req);

    GetReviewPicRes getReviewPic(String reviewPic);
}

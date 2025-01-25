package com.green.acamatch.review;

import com.green.acamatch.review.dto.MyReviewDto;
import com.green.acamatch.review.dto.ReviewDto;
import com.green.acamatch.review.model.ReviewDelReq;
import com.green.acamatch.review.model.ReviewPostReq;
import com.green.acamatch.review.model.ReviewUpdateReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {

    // 리뷰 작성자 확인 (joinClassId 기반)
    Integer isUserAuthorOfReview(long joinClassId, long userId);

    // 학원 ID 존재 여부 확인
    boolean isValidAcademyId(long acaId);

    // join_class_id 유효성 확인
    int isValidJoinClassId(long joinClassId);

    // 수업 참여 여부 확인
    int checkEnrollment(Long joinClassId);

    // 리뷰 등록
    void insertReview(ReviewPostReq req);

    // 리뷰 수정
    int updateReview(ReviewUpdateReq req);

    // 리뷰 삭제 - 학원 관계자 기준
    int deleteReviewByAcademy(ReviewDelReq req);

    // 학원 ID와 리뷰 ID 간의 관계 확인 (서브쿼리 제거)
    Integer isReviewLinkedToAcademy(long joinClassId, long acaId);

    // 리뷰 삭제 - 작성자 기준
    int deleteReviewByUser(ReviewDelReq req);

    // 사용자의 Role ID 가져오기
    Integer getUserRoleId(long userId);

    // 사용자가 요청한 학원과 연결되어 있는지 확인
    Integer isUserLinkedToAcademy(long acaId, long userId);


    // 학원의 리뷰 목록 가져오기
    List<ReviewDto> getAcademyReviews(long acaId);

    // 학원 상세 페이지에서 학원의 리뷰 목록 조회
    List<ReviewDto> getAcademyReviewsForPublic(long acaId);

    // 본인이 작성한 리뷰 목록 조회
    List<MyReviewDto> getReviewsByUserId(long userId);
}
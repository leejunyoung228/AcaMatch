package com.green.acamatch.review;

import com.green.acamatch.review.dto.MyReviewDto;
import com.green.acamatch.review.dto.ReviewDto;
import com.green.acamatch.review.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {

    // 리뷰 작성자 확인 (joinClassId 기반)
    Integer isUserAuthorOfReview(long reviewId, long userId);

    // join_class_id 유효성 확인
    int isValidJoinClassId(long joinClassId);

    int checkClassExists (long classId);

    // 수업 참여 여부 확인
    int checkEnrollment(Long classId, long userId);

    // 리뷰 등록
    void insertReview(Long joinClassId, Long userId, String comment, int star);

    // 리뷰 수정
    int updateReview(ReviewUpdateReq req);

    // 리뷰 삭제 - 학원 관계자 기준
    int deleteReviewByAcademy(ReviewDelMyacademyReq req);

    // 학원 ID와 리뷰 ID 간의 관계 확인
    Integer isReviewLinkedToAcademy(long joinClassId, long acaId);

    // 리뷰 삭제 - 작성자 기준
    int deleteReviewByUser(ReviewDelReq req);

    int checkReviewExists (long reviewId);

    // 특정 유저 ID가 존재하는지 확인
    int checkUserExists(long userId);

    // 사용자가 요청한 학원과 연결되어 있는지 확인
    Integer isUserLinkedToAcademy(long acaId, long userId);

    // 내 학원의 리뷰 목록 가져오기
    List<ReviewDto> getMyAcademyReviews(MyAcademyReviewListGetReq req);

    // 본인이 작성한 리뷰 조회
    List<MyReviewDto> getReviewsByUserId(MyReviewGetReq req);

    // 학원 상세 페이지에서 학원의 리뷰 목록 조회
    List<ReviewDto> getAcademyReviewsForPublic(ReviewListGetReq req);

    // 학원 ID 존재 여부 확인
    Long checkAcaExists(long acaId);

    int checkUserAcademyOwnership(Long userId, Long acaId);

    Long findJoinClassIdByClassAndUser(Long classId, Long userId);

}
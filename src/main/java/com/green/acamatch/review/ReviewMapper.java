package com.green.acamatch.review;

import com.green.acamatch.academy.model.HB.GeneralReviewDto;
import com.green.acamatch.academy.model.HB.MediaReviewDto;
import com.green.acamatch.review.dto.MyReviewDto;
import com.green.acamatch.review.dto.ReviewDto;
import com.green.acamatch.review.model.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReviewMapper {

    int checkEnrollmentByClassIds( List<Long> classIds, Long userId);


    // 리뷰 작성자 확인 (joinClassId 기반)
    int isUserAuthorOfReview(List<Integer> reviewIds, Long userId);

    // join_class_id 유효성 확인
    int isValidJoinClassId(long joinClassId);

    int checkClassExists (long classId);

    // 수업 참여 여부 확인
    int checkEnrollment(Long classId, Long userId);

    // 리뷰 등록
    int insertReview(ReviewPostReq req);

    // 리뷰 수정
    int updateReview(ReviewUpdateReq req);

    // 리뷰 삭제 - 학원 관계자 기준
    int deleteReviewByAcademy(ReviewDelMyacademyReq req);

    // 학원 ID와 리뷰 ID 간의 관계 확인
    Long isReviewLinkedToAcademy(long joinClassId, long acaId);

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
    List<MyReviewDto> getReviewsByUserId(MyMediaReviewGetReq req);

    // 학원 상세 페이지에서 학원의 리뷰 목록 조회
    List<ReviewDto> getAcademyReviewsForPublic(ReviewListGetReq req);

    // 학원 ID 존재 여부 확인
    Long checkClassIdExists(long classId);

    int checkUserAcademyOwnership(Long userId, Long acaId);

    List<Long> findJoinClassIdByAcademyAndUser(Long acaId, Long userId);


    int checkExistingReview(Long acaId, Long userId);

    int checkAcaExists(Long acaId);

   List<Long>  findReviewIdByJoinClassId(Long joinClassId);

   Long  findAcademyIdByReviewId (Long reviewId);

    List<Long> findClassIdByAcaId(Long acaId);

    // 학원 ID와 사용자 ID로 리뷰 ID 목록 조회
    List<Integer> getReviewIdsByAcaIdAndUser(Long acaId, Long userId);

    // 조회된 리뷰 ID 목록을 기반으로 리뷰 삭제
    int deleteReviewByReviewId(List<Integer> reviewIds);

        List<GeneralReviewDto> getGeneralReviews(@Param("generalStartIdx") int generalStartIdx,
                                                 @Param("signedUserId") Long signedUserId,
                                                 @Param("size") int size);

        List<MediaReviewDto> getMediaReviews(@Param("mediaStartIdx") int mediaStartIdx,
                                             @Param("signedUserId") Long signedUserId,
                                             @Param("size") int size);

        int totalMediaReviewCount (Long signedUserId);

}
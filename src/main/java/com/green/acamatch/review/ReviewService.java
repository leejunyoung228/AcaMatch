package com.green.acamatch.review;

import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.review.dto.MyReviewDto;
import com.green.acamatch.review.dto.ReviewDto;
import com.green.acamatch.review.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewMapper mapper;
    private final UserMessage userMessage;

    // 리뷰 등록
    @Transactional
    public int addReview(ReviewPostReq req) {
        if (req.getJoinClassId() == null || req.getJoinClassId() <= 0) {
            userMessage.setMessage("유효하지 않은 수업 참여 ID입니다.");
            return 0;
        }
        if (req.getComment() == null || req.getComment().trim().isEmpty()) {
            req.setComment("");
        }
        if (req.getStar() < 1 || req.getStar() > 5) {
            userMessage.setMessage("별점은 1~5 사이의 값이어야 합니다.");
            return 0;
        }

        int isEnrolled = mapper.checkEnrollment(req.getJoinClassId());
        if (isEnrolled == 0) {
            userMessage.setMessage("유효하지 않은 수업 참여 ID입니다.");
            return 0;
        }

        try {
            mapper.insertReview(req);
        } catch (DuplicateKeyException ex) {
            userMessage.setMessage("이미 해당 수업에 대한 리뷰가 존재합니다.");
            return 0;
        } catch (Exception ex) {
            log.error("리뷰 등록 중 오류 발생: {}", ex.getMessage(), ex);
            userMessage.setMessage("리뷰 등록 중 오류가 발생했습니다.");
            return 0;
        }

        userMessage.setMessage("리뷰 등록이 완료되었습니다.");
        return 1;
    }

    // 리뷰 수정
    @Transactional
    public int updateReview(ReviewUpdateReq req) {
        if (!isValidJoinClassId(req.getJoinClassId())) {
            userMessage.setMessage("유효하지 않은 수업 참여 ID입니다.");
            return 0;
        }

        if (!isEnrolled(req.getJoinClassId())) {
            userMessage.setMessage("해당 수업에 참여한 학생만 리뷰를 수정할 수 있습니다.");
            return 0;
        }

        if (req.getComment() == null || req.getComment().trim().isEmpty()) {
            req.setComment("");
        }

        int rowsUpdated = mapper.updateReview(req);
        if (rowsUpdated == 0) {
            userMessage.setMessage("수정할 리뷰가 존재하지 않거나 권한이 없습니다.");
            return 0;
        }

        userMessage.setMessage("리뷰 수정이 완료되었습니다.");
        return 1;
    }

    // 학원 상세페이지에서 리뷰 조회
    public List<ReviewDto> getAcademyReviewsForPublic(ReviewListGetReq req) {
        // 학원 존재 여부 검증
        if (checkAcaExists(req.getAcaId()) == 0) {
            userMessage.setMessage("유효하지 않은 학원 ID입니다.");
            return Collections.emptyList();
        }

        List<ReviewDto> reviews = mapper.getAcademyReviewsForPublic(req);
        if (reviews == null || reviews.isEmpty()) {
            userMessage.setMessage("리뷰가 존재하지 않습니다.");
            return Collections.emptyList();
        }

        userMessage.setMessage("리뷰 조회가 완료되었습니다.");
        return reviews;
    }

    // 학원 존재 여부 확인
    private int checkAcaExists(long acaId) {
        if (acaId <= 0) {
            userMessage.setMessage("유효하지 않은 학원 ID입니다.");
            return 0;
        }

        int count = mapper.checkAcaExists(acaId); // 매퍼에서 학원 존재 여부 확인
        return count;
    }

    // 학원 관계자가 본인 학원의 리뷰 조회
    public List<ReviewDto> getAcademyReviews(ReviewListGetReq req) {
        if (!isValidAcademyId(req.getAcaId()) || !isUserLinkedToAcademy(req.getAcaId(), req.getUserId())) {
            return Collections.emptyList();
        }

        List<ReviewDto> reviews = mapper.getAcademyReviews(req);
        if (reviews == null || reviews.isEmpty()) {
            userMessage.setMessage("리뷰가 존재하지 않습니다.");
            return Collections.emptyList();
        }

        userMessage.setMessage("학원 리뷰 조회가 완료되었습니다.");
        return reviews;
    }

    // 리뷰 삭제 - 학원 관계자
    @Transactional
    public int deleteReviewByAcademy(ReviewDelReq req) {
        if (!isUserLinkedToAcademy(req.getAcaId(), req.getUserId())) {
            userMessage.setMessage("본인이 관리하는 학원의 리뷰만 삭제할 수 있습니다.");
            return 0;
        }

        int rowsDeleted = mapper.deleteReviewByAcademy(req);
        if (rowsDeleted == 0) {
            userMessage.setMessage("삭제하려는 리뷰가 존재하지 않습니다.");
            return 0;
        }

        userMessage.setMessage("리뷰 삭제가 완료되었습니다.");
        return 1;
    }

    // 리뷰 삭제 - 작성자 본인
    @Transactional
    public int deleteReviewByUser(ReviewDelReq req) {
        if (!isValidUserId(req.getUserId()) || !isUserAuthorOfReview(req.getJoinClassId(), req.getUserId())) {
            userMessage.setMessage("해당 리뷰를 삭제할 권한이 없습니다.");
            return 0;
        }

        int rowsDeleted = mapper.deleteReviewByUser(req);
        if (rowsDeleted == 0) {
            userMessage.setMessage("삭제하려는 리뷰가 존재하지 않습니다.");
            return 0;
        }

        userMessage.setMessage("리뷰 삭제가 완료되었습니다.");
        return 1;
    }

    // 본인이 작성한 리뷰 목록 조회
    public List<MyReviewDto> getReviewsByUserId(MyReviewGetReq req) {
        if (!isValidUserId(req.getUserId())) {
            return Collections.emptyList();
        }

        List<MyReviewDto> reviews = mapper.getReviewsByUserId(req);
        if (reviews == null || reviews.isEmpty()) {
            userMessage.setMessage("작성한 리뷰가 없습니다.");
            return Collections.emptyList();
        }

        userMessage.setMessage("작성한 리뷰 목록 조회가 완료되었습니다.");
        return reviews;
    }

    // 유효성 검증: 학원 ID
    private boolean isValidAcademyId(long acaId) {
        if (acaId <= 0 || !mapper.isValidAcademyId(acaId)) {
            userMessage.setMessage("유효하지 않은 학원 ID입니다.");
            return false;
        }
        return true;
    }

    // 유효성 검증: 유저 ID
    private boolean isValidUserId(long userId) {
        if (userId <= 0 || mapper.getUserRoleId(userId) == null) {
            userMessage.setMessage("유효하지 않은 유저 ID입니다.");
            return false;
        }
        return true;
    }

    // 수업 참여 여부 확인
    private boolean isEnrolled(long joinClassId) {
        return mapper.checkEnrollment(joinClassId) > 0;
    }

    // 유저가 학원에 연결되어 있는지 확인
    private boolean isUserLinkedToAcademy(long acaId, long userId) {
        Integer linked = mapper.isUserLinkedToAcademy(acaId, userId);
        return linked != null && linked > 0;
    }

    // 유저가 리뷰 작성자인지 확인
    private boolean isUserAuthorOfReview(long joinClassId, long userId) {
        Integer isAuthor = mapper.isUserAuthorOfReview(joinClassId, userId);
        return isAuthor != null && isAuthor > 0;
    }

    // join_class_id 유효성 검증
    private boolean isValidJoinClassId(long joinClassId) {
        return mapper.isValidJoinClassId(joinClassId) > 0;
    }
}

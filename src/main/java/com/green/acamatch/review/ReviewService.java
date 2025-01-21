package com.green.acamatch.review;

import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.review.dto.MyReviewDto;
import com.green.acamatch.review.dto.ReviewDto;
import com.green.acamatch.review.model.ReviewDelReq;
import com.green.acamatch.review.model.ReviewPostReq;
import com.green.acamatch.review.model.ReviewUpdateReq;
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
            userMessage.setMessage("리뷰 등록 중 알 수 없는 오류가 발생했습니다.");
            return 0;
        }

        userMessage.setMessage("리뷰 등록이 완료되었습니다.");
        return 1;
    }

    // 리뷰 수정
    @Transactional
    public int updateReview(ReviewUpdateReq req) {
        int isValid = mapper.isValidJoinClassId(req.getJoinClassId());
        if (isValid == 0) {
            userMessage.setMessage("유효하지 않은 수업 참여 ID입니다.");
            return 0;
        }

        int isEnrolled = mapper.checkEnrollment(req.getJoinClassId());
        if (isEnrolled == 0) {
            userMessage.setMessage("해당 수업에 참여한 학생만 리뷰를 수정할 수 있습니다.");
            return 0;
        }

        if (req.getComment() == null || req.getComment().trim().isEmpty()) {
            req.setComment("");
        }


        if (req.getUserId() <= 0) {
            userMessage.setMessage("유효하지 않은 유저 ID입니다.");
            return 0;
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
    public List<ReviewDto> getAcademyReviewsForPublic(long acaId) {
        if (acaId <= 0) {
            userMessage.setMessage("유효하지 않은 학원 ID입니다.");
            return Collections.emptyList();
        }

        boolean isValidAcademy = mapper.isValidAcademyId(acaId);
        if (!isValidAcademy) {
            userMessage.setMessage("존재하지 않는 학원 ID입니다.");
            return Collections.emptyList();
        }

        try {
            List<ReviewDto> reviews = mapper.getAcademyReviewsForPublic(acaId);
            if (reviews == null || reviews.isEmpty()) {
                userMessage.setMessage("리뷰가 존재하지 않습니다.");
                return Collections.emptyList();
            }
            userMessage.setMessage("리뷰 조회가 완료되었습니다.");
            return reviews;
        } catch (Exception ex) {
            userMessage.setMessage("리뷰 조회 중 오류가 발생했습니다.");
            return Collections.emptyList();
        }
    }

    // 학원 관계자가 본인 학원의 리뷰 조회
    public List<ReviewDto> getAcademyReviews(long acaId, long userId) {
        if (acaId <= 0 || userId <= 0) {
            userMessage.setMessage("유효하지 않은 학원 ID 또는 유저 ID입니다.");
            return Collections.emptyList();
        }

        boolean isValidAcademy = mapper.isValidAcademyId(acaId);
        if (!isValidAcademy) {
            userMessage.setMessage("존재하지 않는 학원 ID입니다.");
            return Collections.emptyList();
        }

        Integer isLinked = mapper.isUserLinkedToAcademy(acaId, userId);
        if (isLinked == null || isLinked == 0) {
            userMessage.setMessage("본인이 관리하는 학원의 리뷰만 조회할 수 있습니다.");
            return Collections.emptyList();
        }

        userMessage.setMessage("학원 리뷰 조회가 완료되었습니다.");
        return mapper.getAcademyReviews(acaId);
    }



    // 학원 관계자의 리뷰 삭제
    @Transactional
    public int deleteReviewByAcademy(ReviewDelReq req) {
        if (!validateAcademyAndUserForDelete(req)) {
            return 0;
        }

        int rowsAffected = mapper.deleteReviewByAcademy(req);
        if (rowsAffected == 0) {
            userMessage.setMessage("삭제하려는 리뷰가 존재하지 않습니다.");
            return 0;
        }

        userMessage.setMessage("리뷰 삭제가 완료되었습니다.");
        return 1;
    }

    private boolean validateAcademyAndUserForDelete(ReviewDelReq req) {

        if (req.getUserId() <= 0 || req.getJoinClassId() <= 0) {
            userMessage.setMessage("유효하지 않은 입력 데이터입니다.");
            return false;
        }

        Integer roleId = mapper.getUserRoleId(req.getUserId());
        if (roleId == null || roleId != 3) {
            userMessage.setMessage("해당 유저는 학원 관계자가 아닙니다.");
            return false;
        }

        Integer isLinked = mapper.isUserLinkedToAcademy(req.getAcaId(), req.getUserId());
        if (isLinked == null || isLinked == 0) {
            userMessage.setMessage("본인이 관리하는 학원의 리뷰만 삭제할 수 있습니다.");
            return false;
        }

        return true;
    }

    // 리뷰 작성 유저의 리뷰 삭제
    @Transactional
    public int deleteReviewByUser(ReviewDelReq req) {
        if (!validateInput(req) || !validateReviewAndAcademyRelation(req) || !validateUserIsAuthor(req)) {
            return 0;
        }

        int rowsAffected = mapper.deleteReviewByUser(req);
        if (rowsAffected == 0) {
            userMessage.setMessage("삭제하려는 리뷰가 존재하지 않습니다.");
            return 0;
        }

        userMessage.setMessage("리뷰 삭제가 완료되었습니다.");
        return 1;
    }

    private boolean validateInput(ReviewDelReq req) {
        if (req.getUserId() <= 0 || req.getAcaId() <= 0 || req.getJoinClassId() <= 0) {
            userMessage.setMessage("유효하지 않은 입력 데이터입니다.");
            return false;
        }
        return true;
    }

    private boolean validateReviewAndAcademyRelation(ReviewDelReq req) {
        Integer isLinked = mapper.isReviewLinkedToAcademy(req.getJoinClassId(), req.getAcaId());
        if (isLinked == null || isLinked == 0) {
            userMessage.setMessage("해당 학원에 등록된 리뷰가 아닙니다.");
            return false;
        }
        return true;
    }

    private boolean validateUserIsAuthor(ReviewDelReq req) {
        Integer isAuthor = mapper.isUserAuthorOfReview(req.getJoinClassId(), req.getUserId());
        if (isAuthor == null || isAuthor == 0) {
            userMessage.setMessage("해당 리뷰를 삭제할 권한이 없습니다.");
            return false;
        }
        return true;
    }

    // 본인이 작성한 리뷰 목록 조회
    public List<MyReviewDto> getReviewsByUserId(long userId) {

        if (userId <= 0) {
            userMessage.setMessage("유효하지 않은 유저 ID입니다.");
            return Collections.emptyList();
        }


        // 유저의 역할(role_id)을 확인
        Integer roleId = mapper.getUserRoleId(userId);
        if (roleId == null) {
            userMessage.setMessage("존재하지 않는 유저입니다.");
            return Collections.emptyList();
        }


        List<MyReviewDto> reviews = mapper.getReviewsByUserId(userId);
        if (reviews == null || reviews.isEmpty()) {
            userMessage.setMessage("작성한 리뷰가 없습니다.");
            return Collections.emptyList();
        }

        userMessage.setMessage("작성한 리뷰 목록 조회가 완료되었습니다.");
        return reviews;
    }
}
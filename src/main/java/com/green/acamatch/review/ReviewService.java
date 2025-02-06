package com.green.acamatch.review;

import com.green.acamatch.config.exception.*;
import com.green.acamatch.config.jwt.JwtUser;
import com.green.acamatch.review.dto.MyReviewDto;
import com.green.acamatch.review.dto.ReviewDto;
import com.green.acamatch.review.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewMapper mapper;
    private final UserMessage userMessage;

    /**
     * JWT에서 userId 가져오기
     */
    private JwtUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            userMessage.setMessage("로그인이 필요합니다.");
            throw new CustomException(UserErrorCode.UNAUTHENTICATED);
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof JwtUser) {
            return (JwtUser) principal;
        } else if (principal instanceof String) {
            try {
                return new JwtUser(Long.parseLong((String) principal), Collections.emptyList());
            } catch (NumberFormatException e) {
                userMessage.setMessage("잘못된 인증 정보입니다.");
                throw new CustomException(ReviewErrorCode.UNAUTHENTICATED_USER);
            }
        } else {
            userMessage.setMessage("알 수 없는 사용자 타입입니다.");
            throw new CustomException(ReviewErrorCode.UNAUTHENTICATED_USER);
        }
    }

    /**
     * 리뷰 서비스에서 로그인된 사용자 검증
     */
    private long validateAuthenticatedUser(long requestUserId) {
        long jwtUserId = getAuthenticatedUser().getSignedUserId();

        // 사용자 존재 여부 체크 추가
        validateUserExists(jwtUserId);

        if (jwtUserId != requestUserId) {
            // CustomException에 상세 메시지를 포함하여 던짐
            throw new CustomException(ReviewErrorCode.UNAUTHENTICATED_USER);
        }
        return jwtUserId;
    }

    /**
     * JWT userId와 요청 userId 비교
     */
    private boolean isAuthorizedUser(long requestUserId) {
        long jwtUserId = getAuthenticatedUser().getSignedUserId();

        if (jwtUserId != requestUserId) {
            String errorMessage = String.format("리뷰 서비스: 로그인한 유저의 아이디(%d)와 요청한 유저의 아이디(%d)가 일치하지 않습니다.", jwtUserId, requestUserId);
            userMessage.setMessage(errorMessage);
            return false;
        }
        return true;
    }

    // 리뷰 등록
    @Transactional
    public int addReview(ReviewPostReq req) {
        long jwtUserId = validateAuthenticatedUser(); // JWT에서 가져온 유저 ID 검증
        long requestUserId = req.getUserId();

        // 1. 본인 계정 검증
        if (jwtUserId != requestUserId) {
            userMessage.setMessage("잘못된 요청입니다. 본인의 계정으로만 리뷰를 등록할 수 있습니다.");
            return 0;
        }

        // 2. 유효한 유저인지 확인
        validateUserExists(requestUserId);
        if (!isAuthorizedUser(req.getUserId())) {
            return 0;  //  인증되지 않은 요청이면 바로 종료
        }


        // 3. 수업 참여 여부 확인 (joinClassId 조회)
        Long joinClassId = mapper.findJoinClassIdByClassAndUser(req.getClassId(), requestUserId);
        if (joinClassId == null) {
            userMessage.setMessage("해당 수업에 등록된 기록이 없습니다.");
            return 0;
        }

        // 중복 리뷰 체크
        int existingReviewCount = mapper.checkExistingReview(joinClassId, requestUserId);
        if (existingReviewCount > 0) {
            userMessage.setMessage("이미 해당 수업에 대한 리뷰를 작성하셨습니다.");
            return 0;
        }

        validateReviewRequest(req);
        if (!validateReviewRequest(req)) {
            return 0;
        }


        req.setJoinClassId(joinClassId);

        // 4. 리뷰 등록
        try {

            mapper.insertReview(req);

            log.info("리뷰 등록 성공 - joinClassId: {}, userId: {}", joinClassId, requestUserId);
        } catch (DuplicateKeyException ex) {
            log.error("이미 등록된 리뷰입니다. joinClassId: {}, userId: {}", joinClassId, requestUserId);
            throw new CustomException(ReviewErrorCode.CONFLICT_REVIEW_ALREADY_EXISTS);
        } catch (Exception ex) {
            log.error("리뷰 등록 중 오류 발생 - joinClassId: {}, userId: {}, 오류: {}",
                    joinClassId, requestUserId, ex.getMessage(), ex);
            throw new RuntimeException("리뷰 등록 중 오류가 발생했습니다.", ex);
        }

        userMessage.setMessage("리뷰 등록이 완료되었습니다.");
        return 1;
    }


    /**
     * 리뷰 수정
     */
    @Transactional
    public int updateReview(ReviewUpdateReq req) {
        userMessage.setMessage(null); //  요청 시작 전에 초기화
        log.debug("Updating review for user ID: {}, class ID: {}", req.getUserId(), req.getClassId());

        long jwtUserId = validateAuthenticatedUser(); // JWT에서 가져온 유저 ID 검증
        long requestUserId = req.getUserId();

        // 1. 본인 계정 검증
        if (jwtUserId != requestUserId) {
            userMessage.setMessage("잘못된 요청입니다. 본인의 계정으로만 리뷰를 수정할 수 있습니다.");
            return 0;
        }

        // 유저 존재 여부 확인
        validateUserExists(req.getUserId());

        // 유저 인증 확인
        if (!isAuthorizedUser(req.getUserId())) {
            log.warn("Unauthorized access attempt by user ID: {}", req.getUserId());
            return 0;
        }

        Long joinClassId = mapper.findJoinClassIdByClassAndUser(req.getClassId(), requestUserId);
        if (joinClassId == null) {
            userMessage.setMessage("해당 수업에 등록된 기록이 없습니다.");
            return 0;
        }

        req.setJoinClassId(joinClassId);

        // 리뷰 요청 유효성 검사
        boolean isValid = validateReviewRequest2(req);
        if (!isValid) {
            log.warn("Invalid review update request: {}", req);
            return 0;
        }

        // 유효성 검사 실패 메시지가 존재하는 경우 처리 중단
        if (userMessage.getMessage() != null) {
            log.warn("Validation failed with message: {}", userMessage.getMessage());
            userMessage.setMessage(null); // 메시지 초기화
            return 0;
        }

        // 리뷰 업데이트 수행
        int rowsUpdated = mapper.updateReview(req);
        if (rowsUpdated == 0) {
            userMessage.setMessage("수정할 리뷰를 찾을 수 없습니다.");
            return 0;
        }

        // 데이터 반영 확인
        log.debug("Review update successful for user ID: {}, class ID: {}", req.getUserId(), req.getClassId());
        userMessage.setMessage("리뷰 수정이 완료되었습니다.");
        return 1;
    }

    /**
     * 리뷰 삭제 (작성자 본인)
     */
    @Transactional
    public int deleteReviewByUser(ReviewDelReq req) {
        // 유효성 검사 - classId와 userId는 필수
        if (req.getClassId() == null || req.getUserId() == null) {
            userMessage.setMessage("잘못된 요청입니다. classId와 userId가 필요합니다.");
            return 0;
        }

        long jwtUserId = validateAuthenticatedUser(); // JWT에서 가져온 유저 ID 검증
        long requestUserId = req.getUserId();

        // 1️⃣ 본인 계정 검증
        if (jwtUserId != requestUserId) {
            userMessage.setMessage("잘못된 요청입니다. 본인의 계정으로만 리뷰를 삭제할 수 있습니다.");
            return 0;
        }

        // 2️⃣ 유저 존재 여부 확인
        validateUserExists(req.getUserId());

        if (!isAuthorizedUser(req.getUserId())) {
            return 0; // 인증되지 않은 요청이면 종료
        }

        // 3️⃣ 수업 존재 여부 확인
        if (mapper.checkClassExists(req.getClassId()) == 0) {
            userMessage.setMessage("존재하지 않는 수업입니다.");
            return 0;
        }

        // 4️⃣ 유저가 해당 수업을 수강했는지 확인
        if (mapper.checkEnrollment(req.getClassId(), req.getUserId()) == 0) {
            userMessage.setMessage("해당 수업을 수강하지 않았습니다.");
            return 0;
        }

        // 5️⃣ `joinClassId` 자동 조회
        Long joinClassId = mapper.findJoinClassIdByClassAndUser(req.getClassId(), req.getUserId());

        if (joinClassId == null) {
            userMessage.setMessage("삭제할 리뷰가 없습니다.");
            return 0;
        }

        // 6️⃣ `reviewId` 자동 조회
        Long reviewId = mapper.findReviewIdByJoinClassId(joinClassId);

        if (reviewId == null) {
            userMessage.setMessage("이미 삭제된 리뷰입니다.");
            return 0;
        }

        // 7️⃣ 작성자인지 검증
        if (!isUserAuthorOfReview(joinClassId, req.getUserId())) {
            userMessage.setMessage("해당 리뷰의 작성자가 아닙니다. 삭제할 권한이 없습니다.");
            return 0;
        }

        // 8️⃣ 리뷰 삭제 수행
        int rowsDeleted = mapper.deleteReviewByUser(req);

        if (rowsDeleted == 0) {
            userMessage.setMessage("삭제할 리뷰를 찾을 수 없습니다.");
            return 0;
        }

        userMessage.setMessage("리뷰 삭제가 완료되었습니다.");
        return 1;
    }


    /**
     * 리뷰 삭제 (학원 관계자)
     */
    @Transactional

    public int deleteReviewByAcademy(ReviewDelMyacademyReq req) {
        // 학원 ID(acaId)와 리뷰 ID(reviewId) 검증
        if (req.getAcaId() == null) {
            userMessage.setMessage("학원 ID(acaId)가 누락되었습니다.");
            return 0;
        }

        if (req.getReviewId() == null) {
            userMessage.setMessage("리뷰 ID가 누락되었습니다.");
            return 0;
        }

        long jwtUserId = validateAuthenticatedUser(); // JWT에서 가져온 유저 ID 검증
        long requestUserId = req.getUserId();
        // 1. 본인 계정 검증
        if (jwtUserId != requestUserId) {
            userMessage.setMessage("잘못된 요청입니다. 로그인 한 유저는 해당 학원의 관리자가 아닙니다.");
            return 0;
        }

        if (mapper.checkAcaExists(req.getAcaId()) == null || req.getAcaId() == 0) {
            userMessage.setMessage("존재하지 않는 학원입니다");
            return 0;
        }
        validateUserExists(req.getUserId());
        if (!isAuthorizedUser(req.getUserId())) {
            return 0;  //  인증되지 않은 요청이면 바로 종료
        }



        //  학원 관계자 권한 확인
        if (!isUserLinkedToAcademy(req.getAcaId(), req.getUserId())) {
            userMessage.setMessage("해당 학원을 관리할 권한이 없습니다.");
            return 0;
        }

        // 해당 리뷰가 존재하는지 확인
        if (mapper.checkReviewExists(req.getReviewId()) == 0) {
            userMessage.setMessage("삭제할 리뷰가 존재하지 않습니다.");
            return 0;
        }



        //  해당 리뷰가 해당 학원에 속하는지 확인
        if (!isReviewLinkedToAcademy(req.getReviewId(), req.getAcaId())) {
            userMessage.setMessage("해당 리뷰는 요청한 학원에 속해 있지 않습니다.");
            return 0;
        }

        // 리뷰 삭제 수행
        int rowsDeleted = mapper.deleteReviewByAcademy(req);
        if (rowsDeleted == 0) {
            userMessage.setMessage("삭제할 리뷰를 찾을 수 없습니다.");
            return 0;
        }

        // 리뷰 삭제 완료 메시지
        userMessage.setMessage("리뷰 삭제가 완료되었습니다.");
        return 1;
    }


    /**
     * 학원 관리자의 자신의 모든 학원 리뷰 조회 (로그인 필요)
     */
    @Transactional
    public List<ReviewDto> getMyAcademyReviews(MyAcademyReviewListGetReq req) {

        //  유저 존재 여부 확인 (추가)
        validateUserExists(req.getUserId());
        if (!isAuthorizedUser(req.getUserId())) {
            return Collections.emptyList();  //  인증되지 않은 요청이면 바로 종료
        }


        validateAuthenticatedUser(req.getUserId());


//        // 학원 관계자 권한 검증 (본인이 관리하는 학원의 리뷰만 조회 가능)
        checkUserAcademyOwnership(req.getAcaId(), req.getUserId());



        List<ReviewDto> reviews = mapper.getMyAcademyReviews(req);
        if (reviews.isEmpty()) {
            userMessage.setMessage("리뷰가 존재하지 않습니다.");
            return Collections.emptyList();
        }

        userMessage.setMessage("리뷰 조회가 완료되었습니다.");
        return reviews;
    }


    /**
     * 본인이 작성한 리뷰 목록 조회
     */
    @Transactional
    public List<MyReviewDto> getReviewsByUserId(MyReviewGetReq req) {
        long jwtUserId = validateAuthenticatedUser(); // JWT에서 가져온 유저 ID 검증
        long requestUserId = req.getUserId();

        // 1. 본인 계정 검증
        if (jwtUserId != requestUserId) {
            userMessage.setMessage("잘못된 요청입니다. 본인의 계정으로만 리뷰를 삭제할 수 있습니다.");
            return Collections.emptyList();
        }
        validateUserExists(req.getUserId());

        if (!isAuthorizedUser(req.getUserId())) {
            return Collections.emptyList();  //  인증되지 않은 요청이면 빈 리스트 반환
        }
        //  유저 존재 여부 확인 (추가)

        List<MyReviewDto> reviews = mapper.getReviewsByUserId(req);
        if (reviews.isEmpty()) {
            userMessage.setMessage("작성한 리뷰가 없습니다.");
            return Collections.emptyList();
        }

        userMessage.setMessage("작성한 리뷰 목록 조회가 완료되었습니다.");
        return reviews;
    }

    /**
     * 공개 학원 리뷰 조회 (로그인 필요 없음)
     */
    @Transactional
    public List<ReviewDto> getAcademyReviewsForPublic(ReviewListGetReq req) {
        validateAcademy(req.getAcaId());

        List<ReviewDto> reviews = mapper.getAcademyReviewsForPublic(req);
        if (reviews.isEmpty()) {
            userMessage.setMessage("리뷰가 존재하지 않습니다.");
            return Collections.emptyList();
        }

        userMessage.setMessage("리뷰 조회가 완료되었습니다.");
        return reviews;
    }

    /**
     * 로그인된 사용자 검증 (로그인 안 했으면 예외 발생)
     */
    private long validateAuthenticatedUser() {
        long jwtUserId = getAuthenticatedUser().getSignedUserId();

        //  유저 ID가 0이면 예외 처리 (잘못된 토큰이거나 요청)
        if (jwtUserId == 0) {
            throw new CustomException(ReviewErrorCode.INVALID_USER);
        }

        return jwtUserId;
    }

    /* 유효성 검사 */


    /**
     * 사용자 ID가 DB에 존재하는지 확인
     */
    private void validateUserExists(long userId) {
        if (mapper.checkUserExists(userId) == 0) {
            userMessage.setMessage("유효하지 않은 유저 ID입니다.");
            throw new CustomException(UserErrorCode.USER_NOT_FOUND);
        }
    }


    private boolean validateReviewRequest(ReviewPostReq req) {
        // 1. 수업 참여 ID 조회 (classId + userId 기반으로 조회)
        Long joinClassId = mapper.findJoinClassIdByClassAndUser(req.getClassId(), req.getUserId());

        if (joinClassId == null) {
            userMessage.setMessage("해당 수업에 등록된 기록이 없습니다.");
            return false;
        }

        // 2. 사용자가 수업을 정상적으로 수료했는지 확인
        if (mapper.checkEnrollment(req.getClassId(), req.getUserId()) == 0) {
            userMessage.setMessage("수업에 참여한 사용자만 리뷰를 작성할 수 있습니다.");
            return false;
        }

        // 3. 별점 유효성 검사
        if (req.getStar() < 1 || req.getStar() > 5) {
            userMessage.setMessage("별점은 1~5 사이의 값이어야 합니다.");
            return false;
        }

        // 4. 리뷰 내용 검증 (빈 문자열 허용)
        if (req.getComment() == null || req.getComment().trim().isEmpty()) {
            req.setComment(""); // 빈 문자열로 설정
        }

        if (req.getReviewId() != null) {
            userMessage.setMessage("");
            return false;
        }
        return true;
    }
    
    private boolean validateReviewRequest2(ReviewUpdateReq req) {
        if (req.getClassId() == null || req.getClassId() <= 0 || mapper.isValidJoinClassId(req.getJoinClassId()) == 0) {
            userMessage.setMessage("유효하지 않은 수업 참여 ID입니다.");
            return false;
        }
        if (mapper.checkEnrollment(req.getClassId(), req.getUserId()) == 0) {
            userMessage.setMessage("수업에 참여한 사용자만 리뷰를 작성할 수 있습니다.");
            return false;
        }
        if (req.getStar() < 1 || req.getStar() > 5) {
            userMessage.setMessage("별점은 1~5 사이의 값이어야 합니다.");
            return false;
        }
        if (req.getComment() == null || req.getComment().trim().isEmpty()) {
            req.setComment(""); // 빈 문자열로 설정
        }
        return true;
    }

    private boolean validateReviewRequest3(ReviewDelReq req) {

        // 해당 사용자가 수업을 수료했는지 확인 (필요 시 추가)
        if (mapper.checkEnrollment(req.getClassId(), req.getUserId()) == 0) {
            userMessage.setMessage("수업을 수료한 사용자만 리뷰를 삭제할 수 있습니다.");
            return false;
        }

        return true;
    }


    private void validateAcademy(long acaId) {
        if (acaId <= 0 || mapper.checkAcaExists(acaId) == 0) {
            throw new CustomException(ReviewErrorCode.INVALID_ACADEMY);
        }
    }

    /**  리뷰 작성자인지 검증 */
    private void validateReviewAuthor(long reviewId, long userId) {
        if (!isUserAuthorOfReview(reviewId, userId)) {
            throw new CustomException(ReviewErrorCode.UNRIGHT_USER);
        }
    }

    /**  해당 유저가 리뷰 작성자인지 확인 */
    private boolean isUserAuthorOfReview(long joinClassId, long userId) {
        Integer isAuthor = mapper.isUserAuthorOfReview(joinClassId, userId);
        return isAuthor != null && isAuthor > 0;
    }

    /**  학원 관계자 권한 검증 */
    private void checkUserAcademyOwnership(long acaId, long userId) {
        if (!isUserLinkedToAcademy(acaId, userId)) {
            throw new CustomException(ReviewErrorCode.UNAUTHORIZED_ACADEMY_ACCESS);
        }
    }

    private boolean isUserLinkedToAcademy(long acaId, long userId) {
        Integer result = mapper.isUserLinkedToAcademy(acaId, userId);
        return result != null && result > 0;
    }

    /**  리뷰가 학원에 속하는지 확인 */
    private boolean isReviewLinkedToAcademy(long joinClassId, long acaId) {
        Integer result = mapper.isReviewLinkedToAcademy(joinClassId, acaId);
        return result != null && result > 0;
    }


}
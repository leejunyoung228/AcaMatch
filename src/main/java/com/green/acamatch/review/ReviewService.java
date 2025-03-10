package com.green.acamatch.review;

import com.green.acamatch.acaClass.ClassRepository;
import com.green.acamatch.academy.AcademyRepository;
import com.green.acamatch.accessLog.dailyVisitorStatus.CustomUserDetails;
import com.green.acamatch.config.exception.*;
import com.green.acamatch.config.jwt.JwtUser;
import com.green.acamatch.config.security.AuthenticationFacade;
import com.green.acamatch.entity.acaClass.AcaClass;
import com.green.acamatch.entity.joinClass.JoinClass;
import com.green.acamatch.entity.myenum.UserRole;
import com.green.acamatch.entity.review.Review;
import com.green.acamatch.entity.user.Relationship;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.joinClass.JoinClassRepository;
import com.green.acamatch.review.dto.MyReviewDto;
import com.green.acamatch.review.dto.ReviewDto;
import com.green.acamatch.review.model.*;
import com.green.acamatch.user.repository.RelationshipRepository;
import com.green.acamatch.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;

import javax.management.relation.Role;


@Service
@RequiredArgsConstructor

public class ReviewService {

    private final ReviewMapper mapper;
    private final UserMessage userMessage;
    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);
    private final RelationshipRepository relationshipRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final JoinClassRepository joinClassRepository;
    private final ClassRepository acaClassRepository;
    private final AcademyRepository academyRepository;


//    /**
//     * 리뷰 서비스에서 로그인된 사용자 검증
//     */
//    private long validateAuthenticatedUser(long requestUserId) {
//        long jwtUserId = AuthenticationFacade.getSignedUserId();
//
//        // 사용자 존재 여부 체크 추가
//        validateUserExists(jwtUserId);
//
//        if (jwtUserId != requestUserId) {
//            // CustomException에 상세 메시지를 포함하여 던짐
//            throw new CustomException(ReviewErrorCode.UNAUTHENTICATED_USER);
//        }
//        return jwtUserId;
//    }

    private Long validateAuthenticatedUser(long requestUserId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            log.warn("인증되지 않은 사용자 요청");
            throw new CustomException(ReviewErrorCode.INVALID_USER);
        }

        Object principal = auth.getPrincipal();
        log.debug("Principal 정보: {}", principal);

        if (principal instanceof JwtUser) {
            log.debug("JWT에서 추출한 userId: {}", ((JwtUser) principal).getSignedUserId());
            return ((JwtUser) principal).getSignedUserId();
        } else if (principal instanceof CustomUserDetails) {
            log.debug("CustomUserDetails에서 추출한 userId: {}", ((CustomUserDetails) principal).getUserId());
            return ((CustomUserDetails) principal).getUserId();
        }

        log.warn("userId를 찾을 수 없음");
        throw new CustomException(UserErrorCode.USER_NOT_FOUND);
    }


    /**
     * JWT userId와 요청 userId 비교
     */
    private boolean isAuthorizedUser(long requestUserId) {
        long jwtUserId = AuthenticationFacade.getSignedUserId();

        if (jwtUserId != requestUserId) {
            String errorMessage = String.format("리뷰 서비스: 로그인한 유저의 아이디(%d)와 요청한 유저의 아이디(%d)가 일치하지 않습니다.", jwtUserId, requestUserId);
            userMessage.setMessage(errorMessage);
            return false;
        }
        return true;
    }

    private boolean isStudent(Long userId) {
        return userRepository.existsByUserIdAndUserRole(userId, UserRole.STUDENT);
    }

    @Transactional
    public int createReview(@Valid ReviewPostReqForParent req) {
        long requestUserId = req.getUserId();
        long jwtUserId = validateAuthenticatedUser(requestUserId);  // 인증된 사용자 검증

        // 본인 계정 검증
        if (jwtUserId != requestUserId) {
            userMessage.setMessage("잘못된 요청입니다. 본인의 계정으로만 리뷰를 등록할 수 있습니다.");
            return 0;
        }


        // 사용자 존재 여부 확인
        validateUserExists(requestUserId);

        // 수업 등록 여부 확인
        if (joinClassRepository.findStudentsByClassId(req.getClassId()).isEmpty()) {
            userMessage.setMessage("해당 수업에 등록된 학생이 아닙니다.");
            return 0;
        }

        // 중복 리뷰 작성 방지
        if (reviewRepository.existsByJoinClass_AcaClass_ClassIdAndUser_UserId(req.getClassId(), requestUserId)) {
            userMessage.setMessage("이미 해당 학원에 대한 리뷰를 작성하셨습니다.");
            return 0;
        }

        // 리뷰 작성자 검증 (학생 또는 학부모)
        User reviewWriter;
        if (isStudent(requestUserId)) {
            reviewWriter = userRepository.findByUserId(requestUserId)
                    .orElseThrow(() -> new CustomException(ReviewErrorCode.INVALID_USER));
        } else {
            if (req.getStudentId() == null) {
                userMessage.setMessage("유효하지 않은 학생 정보입니다.");
                return 0;
            }

            Relationship relationship = relationshipRepository.findByParentUserIdAndStudentUserIdAndCertification(
                            requestUserId, req.getStudentId(), req.getCertification())
                    .orElseThrow(() -> new CustomException(ReviewErrorCode.NOT_STUDENT_PARENT));

            reviewWriter = relationship.getParent();
        }

        // 학원 수업 정보 조회
        AcaClass acaClass = acaClassRepository.findById(req.getClassId())
                .orElseThrow(() -> new CustomException(ReviewErrorCode.CLASS_NOT_FOUND));

        User user = userRepository.findById(requestUserId)
                .orElseThrow(() -> new CustomException(ReviewErrorCode.INVALID_USER));

        // JoinClass 조회
        JoinClass joinClass = joinClassRepository.findByAcaClassAndUser(acaClass, user)
                .orElseThrow(() -> {
                    userMessage.setMessage("해당 수업을 찾을 수 없습니다.");
                    return new CustomException(ReviewErrorCode.CLASS_NOT_FOUND);
                });

        // 별점 유효성 검사 (null 불가, 1~5 범위 유지)
        if (req.getStar() == null || req.getStar() < 1 || req.getStar() > 5) {
            userMessage.setMessage("별점은 1~5 사이의 값이어야 합니다.");
            return 0;
        }

        // 댓글 유효성 검사 (null이면 빈 문자열 설정)
        if (req.getComment() == null || req.getComment().trim().isEmpty()) {
            req.setComment("");
        }

        // 새로운 리뷰 객체 생성 및 저장
        Review newReview = new Review();
        newReview.setUser(reviewWriter);
        newReview.setJoinClass(joinClass);
        newReview.setComment(req.getComment());
        newReview.setStar(req.getStar());
        newReview.setBanReview(0); // 기본값 0 설정

        reviewRepository.save(newReview);

        userMessage.setMessage("리뷰가 성공적으로 등록되었습니다.");
        return 1;
    }


//    // 리뷰 등록
//    @Transactional
//    public int addReview(ReviewPostReq req) {
//        long jwtUserId = validateAuthenticatedUser();
//        long requestUserId = req.getUserId();
//
//        // 본인 계정 검증
//        if (jwtUserId != requestUserId) {
//            userMessage.setMessage("잘못된 요청입니다. 본인의 계정으로만 리뷰를 등록할 수 있습니다.");
//            return 0;
//        }
//
//        // 유저 존재 여부 확인
//        validateUserExists(requestUserId);
//
//        if (!isAuthorizedUser(req.getUserId())) {
//            return 0;
//        }
//
//        if (mapper.checkAcaExists(req.getAcaId()) == 0) {
//            userMessage.setMessage("유효하지 않은 학원 ID입니다.");
//            return 0;
//        }
//
//        if (mapper.checkUserExists(req.getUserId()) == 0) {
//            userMessage.setMessage("유효하지 않은 유저 ID입니다.");
//            return 0;
//        }
//
////        // 본인 학원인지 먼저 검증 (가장 먼저 실행!)
////        if (isUserLinkedToAcademy(req.getAcaId(), req.getUserId())) {
////            userMessage.setMessage("해당 학원은 본인의 학원이므로 리뷰를 남길 수 없습니다.");
////            return 0;
////        }
//
//        // 학원에 등록된 수업 조회
//        List<Long> classIds = mapper.findClassIdByAcaId(req.getAcaId());
//        log.info("클래스 ID 리스트: {}", classIds);
//
//        if (classIds.isEmpty()) {
//            userMessage.setMessage("해당 학원에 등록된 수업이 없습니다.");
//            return 0;
//        }
//
//        // 유저가 해당 학원의 수업을 수강했는지 확인
//        List<Long> joinClassIds = mapper.findJoinClassIdByAcademyAndUser(req.getAcaId(), requestUserId);
//        log.info("joinClassId 리스트: {}", joinClassIds);
//
//        if (joinClassIds.isEmpty()) {
//            userMessage.setMessage("해당 학원의 수업을 수강한 기록이 없습니다. 수강한 후 리뷰를 작성할 수 있습니다.");
//            return 0;
//        }
//
//        Long joinClassId = joinClassIds.get(0);
//        log.info(" 최종 joinClassId 값: {}", joinClassId);
//
//        // 이미 리뷰를 작성했는지 체크
//        int existingReviewCount = mapper.checkExistingReview(req.getAcaId(), requestUserId);
//        if (existingReviewCount > 0) {
//            userMessage.setMessage("이미 해당 학원에 대한 리뷰를 작성하셨습니다.");
//            return 0;
//        }
//
//        // 별점 범위 검증
//        if (req.getStar() < 1 || req.getStar() > 5) {
//            userMessage.setMessage("별점은 1~5 사이의 값이어야 합니다.");
//            return 0;
//        }
//
//        // 댓글이 없을 경우 빈 문자열로 설정
//        if (req.getComment() == null || req.getComment().trim().isEmpty()) {
//            req.setComment("");
//        }
//
//        // 리뷰 등록
//        int rowsInserted = mapper.insertReview(req);
//        if (rowsInserted == 0) {
//            userMessage.setMessage("리뷰 등록에 실패했습니다.");
//            return 0;
//        }
//
//        userMessage.setMessage("리뷰가 성공적으로 등록되었습니다.");
//        return 1;
//    }


    //리뷰 수정
    @Transactional
    public int updateReview(@Valid ReviewUpdateReq req) {
        long jwtUserId = validateAuthenticatedUser();
        long requestUserId = req.getUserId();

        try {
            // 본인 계정 검증
            if (jwtUserId != requestUserId) {
                userMessage.setMessage("잘못된 요청입니다. 본인의 계정으로만 리뷰를 수정할 수 있습니다.");
                return 0;
            }

            // 기존 리뷰 조회
            Review existingReview = reviewRepository.findById(req.getReviewId())
                    .orElseThrow(() -> {
                        userMessage.setMessage("수정할 리뷰를 찾을 수 없습니다.");
                        return new CustomException(ReviewErrorCode.INVALID_REVIEW_DATA);
                    });

            // 리뷰 작성자 검증
            if (!existingReview.getUser().getUserId().equals(requestUserId)) {
                userMessage.setMessage("본인의 리뷰만 수정할 수 있습니다.");
                return 0;
            }

            // 학원 존재 여부 검증 (수강 기록 확인)
            boolean acaExists = joinClassRepository.existsByAcaClass_ClassIdAndUser_UserId(req.getClassId(), requestUserId);
            if (!acaExists) {
                userMessage.setMessage("해당 학원의 수업을 수강한 기록이 없습니다.");
                return 0;
            }

            // 별점 유지 (null이면 기존 값 유지)
            if (req.getStar() != null) {
                if (req.getStar() < 1 || req.getStar() > 5) {
                    userMessage.setMessage("별점은 1~5 사이의 값이어야 합니다.");
                    return 0;
                }
                existingReview.setStar(req.getStar());
            }

            // 댓글 유지 (null이면 기존 값 유지)
            if (req.getComment() != null) {
                existingReview.setComment(req.getComment());
            }

            reviewRepository.save(existingReview);
            userMessage.setMessage("리뷰 수정이 완료되었습니다.");
            return 1;

        } catch (Exception e) {
            userMessage.setMessage("리뷰 수정 중 오류가 발생했습니다: " + e.getMessage());
            return 0;
        }
    }


//    /**
//     * 리뷰 수정
//     */
//    @Transactional
//    public int updateReview(ReviewUpdateReq req) {
//        userMessage.setMessage(null); //  요청 시작 전에 초기화
//        log.debug("Updating review for user ID: {}, class ID: {}", req.getUserId(), req.getAcaId());
//
//        long jwtUserId = validateAuthenticatedUser(); // JWT에서 가져온 유저 ID 검증
//        long requestUserId = req.getUserId();
//
//        // 1. 본인 계정 검증
//        if (jwtUserId != requestUserId) {
//            userMessage.setMessage("잘못된 요청입니다. 본인의 계정으로만 리뷰를 수정할 수 있습니다.");
//            return 0;
//        }
//
//        // 유저 존재 여부 확인
//        validateUserExists(req.getUserId());
//
//
//        if (mapper.checkAcaExists(req.getAcaId()) == 0) {
//            userMessage.setMessage("유효하지 않은 학원 ID입니다.");
//            return 0;
//        }
//
//        if (mapper.checkUserExists(req.getUserId()) == 0) {
//            userMessage.setMessage("유효하지 않은 유저 ID입니다.");
//            return 0;
//        }
//
//        // 유저 인증 확인
//        if (!isAuthorizedUser(req.getUserId())) {
//            log.warn("Unauthorized access attempt by user ID: {}", req.getUserId());
//            return 0;
//        }
//
//        // 학원 ID 검증
//        Long acaId = req.getAcaId();
//        if (acaId == null) {
//            userMessage.setMessage("학원 ID가 제공되지 않았습니다.");
//            log.error("AcaId is null for userId: {}", requestUserId);
//            return 0;
//        }
//
//        List<Long> classIds = mapper.findClassIdByAcaId(req.getAcaId());
//        log.info("클래스 ID 리스트: {}", classIds);
//
//        //  학원에 수업이 하나라도 있는지 확인
//        if (classIds.isEmpty()) {
//            userMessage.setMessage("해당 학원에 등록된 수업이 없습니다.");
//            log.warn("No classes found for acaId: {}", req.getAcaId());
//            return 0;
//        }
//
//        //  첫 번째 수업 ID 선택 (NULL 방지)
//        Optional<Long> classIdOptional = classIds.stream().findFirst();
//        if (!classIdOptional.isPresent()) {
//            log.error("classId가 NULL입니다!");
//            return 0;
//        }
//        Long classId = classIdOptional.get();
//        log.info("최종 classId 값: {}", classId);
//
//
//        List<Long> joinClassIds = mapper.findJoinClassIdByAcademyAndUser(req.getAcaId(), requestUserId);
//        log.info("joinClassId 리스트: {}", joinClassIds);
//
//        Optional<Long> joinClassIdOptional = joinClassIds.stream().findFirst();
//        if (!joinClassIdOptional.isPresent()) {
//            userMessage.setMessage("해당 학원에 등록된 기록이 없습니다.");
//            log.error("joinClassId가 NULL입니다!");
//            return 0;
//        }
//
//        Long joinClassId = joinClassIdOptional.get();
//        log.info("최종 joinClassId 값: {}", joinClassId);
//        log.info("최종 classId 값: {}", classId);
//
//
//        if (req.getStar() < 1 || req.getStar() > 5) {
//            userMessage.setMessage("별점은 1~5 사이의 값이어야 합니다.");
//            return 0;
//        }
//
//        if (req.getComment() == null || req.getComment().trim().isEmpty()) {
//            req.setComment(""); // 빈 문자열로 설정
//        }

////        // 리뷰 요청 유효성 검사
////        boolean isValid = validateReviewRequest2(req);
////        if (!isValid) {
////            log.warn("Invalid review update request: {}", req);
////            return 0;
////        }
//
//        // 유효성 검사 실패 메시지가 존재하는 경우 처리 중단
//
//        req.setJoinClassId(joinClassId);
//
//        if (userMessage.getMessage() != null) {
//            log.warn("Validation failed with message: {}", userMessage.getMessage());
//            userMessage.setMessage(null); // 메시지 초기화
//            return 0;
//        }
//
//        // 리뷰 업데이트 수행
//        int rowsUpdated = mapper.updateReview(req);
//        if (rowsUpdated == 0) {
//            userMessage.setMessage("수정할 리뷰를 찾을 수 없습니다.");
//            return 0;
//        }
//
//        // 데이터 반영 확인
//        log.debug("Review update successful for user ID: {}, class ID: {}", req.getUserId(), req.getAcaId());
//        userMessage.setMessage("리뷰 수정이 완료되었습니다.");
//        return 1;
//    }


    //리뷰 삭제 (작성자 본인)

    @Transactional
    public int deleteReviewByUser(@Valid ReviewDelReq req) {
        // 필수값 검증
        if (req.getClassId() == null || req.getUserId() == null) {
            userMessage.setMessage("잘못된 요청입니다. classId와 userId가 필요합니다.");
            return 0;
        }

        long jwtUserId = validateAuthenticatedUser();
        long requestUserId = req.getUserId();

        // 본인 계정 검증
        if (jwtUserId != requestUserId) {
            userMessage.setMessage("본인의 계정으로만 리뷰를 삭제할 수 있습니다.");
            return 0;
        }

        // 수업 존재 여부 확인
        Optional<AcaClass> optionalClass = acaClassRepository.findById(req.getClassId());
        if (optionalClass.isEmpty()) {
            userMessage.setMessage("해당 수업(classId)이 존재하지 않습니다.");
            return 0;
        }

        // 사용자가 해당 수업을 수강한 기록이 있는지 검증
        boolean isEnrolled = joinClassRepository.existsByClassIdAndUserId(req.getClassId(), requestUserId);
        if (!isEnrolled) {
            userMessage.setMessage("해당 수업을 수강한 기록이 없습니다.");
            return 0;
        }

        // 삭제할 리뷰 조회
        List<Review> reviews = reviewRepository.findByJoinClass_AcaClass_ClassIdAndUser_UserId(req.getClassId(), requestUserId);
        if (reviews.isEmpty()) {
            userMessage.setMessage("삭제할 리뷰가 없습니다.");
            return 0;
        }

        // 리뷰 작성자 검증
        boolean isAuthor = reviews.stream().allMatch(review -> review.getUser().getUserId().equals(requestUserId));
        if (!isAuthor) {
            userMessage.setMessage("해당 리뷰의 작성자가 아닙니다. 삭제 권한이 없습니다.");
            return 0;
        }

        // 리뷰 삭제 수행
        reviewRepository.deleteAll(reviews);
        userMessage.setMessage("리뷰 삭제가 완료되었습니다.");

        log.info("사용자(userId: {})가 수업(classId: {})에 대한 리뷰 삭제 완료!",
                requestUserId, req.getClassId());
        return 1;
    }


//    //  리뷰 삭제 (작성자 본인)
//
//    @Transactional
//    public int deleteReviewByUser(ReviewDelReq req) {
//        // 필수 파라미터 확인
//        if (req.getAcaId() == null || req.getUserId() == null) {
//            userMessage.setMessage("잘못된 요청입니다. acaId와 userId가 필요합니다.");
//            return 0;
//        }
//
//        long jwtUserId = validateAuthenticatedUser();
//        long requestUserId = req.getUserId();
//
//        // 본인 계정 검증
//        if (jwtUserId != requestUserId) {
//            userMessage.setMessage("잘못된 요청입니다. 본인의 계정으로만 리뷰를 삭제할 수 있습니다.");
//            return 0;
//        }
//
//        // 유저 존재 여부 확인
//        if (mapper.checkUserExists(requestUserId) == 0) {
//            userMessage.setMessage("유효하지 않은 유저 ID입니다.");
//            return 0;
//        }
//
//        // 학원 존재 여부 확인
//        List<Long> classIds = mapper.findClassIdByAcaId(req.getAcaId()); // acaId 기준으로 classId 조회
//        log.info(" 학원(acaId: {})에 속한 클래스 ID 리스트: {}", req.getAcaId(), classIds);
//
//        if (classIds.isEmpty()) {
//            userMessage.setMessage("해당 학원에 등록된 수업이 없습니다.");
//            return 0;
//        }
//
//        // 올바른 class_id 리스트를 가져와서 JOINCLASS 확인
//        int enrollmentCheck = mapper.checkEnrollmentByClassIds(classIds, requestUserId);
//        if (enrollmentCheck == 0) {
//            userMessage.setMessage("해당 학원의 수업을 수강한 기록이 없습니다.");
//            return 0;
//        }
//
//        // joinClassId 조회
//        List<Long> joinClassIds = mapper.findJoinClassIdByAcademyAndUser(req.getAcaId(), requestUserId);
//        log.info("joinClassId 리스트: {}", joinClassIds);
//
//        if (joinClassIds.isEmpty()) {
//            userMessage.setMessage("해당 학원에 등록된 기록이 없습니다.");
//            return 0;
//        }
//
//        // 리뷰 ID 조회
//        List<Integer> reviewIds = mapper.getReviewIdsByAcaIdAndUser(req.getAcaId(), requestUserId);
//        if (reviewIds.isEmpty()) {
//            userMessage.setMessage("삭제할 리뷰가 없습니다.");
//            log.warn(" 삭제할 리뷰가 없습니다. reviewId가 NULL입니다.");
//            return 0;
//        }
//
//        // 작성자 확인
//        if (!reviewIds.isEmpty() && !isUserAuthorOfReview(reviewIds, requestUserId)) {
//            userMessage.setMessage("해당 리뷰의 작성자가 아닙니다. 삭제할 권한이 없습니다.");
//            return 0;
//        }
//
//        // 리뷰 삭제 수행
//        int rowsDeleted = mapper.deleteReviewByReviewId(reviewIds);
//        if (rowsDeleted == 0) {
//            userMessage.setMessage("삭제할 리뷰를 찾을 수 없습니다.");
//            return 0;
//        }
//
//        log.info(" 학원(acaId: {})에 대한 사용자(userId: {}) 리뷰 삭제 완료!", req.getAcaId(), requestUserId);
//        userMessage.setMessage("리뷰 삭제가 완료되었습니다.");
//        return 1;
//    }
//

    // 학원 관계자의 자기 학원에 달린 리뷰 삭제
    @Transactional
    public int deleteReviewByAcademyAndAdmin(ReviewDelMyacademyReq req) {
        if (req.getClassId() == null) {
            userMessage.setMessage("수업 ID(classId)가 누락되었습니다.");
            return 0;
        }
        if (req.getReviewId() == null) {
            userMessage.setMessage("리뷰 ID가 누락되었습니다.");
            return 0;
        }

        long jwtUserId = validateAuthenticatedUser();
        long requestUserId = req.getUserId();

        if (jwtUserId != requestUserId) {
            userMessage.setMessage("본인의 계정으로만 학원 리뷰를 관리할 수 있습니다.");
            return 0;
        }

        if (isAdmin(jwtUserId)) {
            reviewRepository.deleteById(req.getReviewId());
            userMessage.setMessage("리뷰가 삭제되었습니다. (최종 관리자)");
            log.info("최종 관리자(userId: {})가 리뷰(reviewId: {}) 삭제 완료!", jwtUserId, req.getReviewId());
            return 1;
        }


        // acaId가 없으면 classId를 통해 학원 ID 조회
        Long acaId = req.getAcaId();
        if (acaId == null) {
            acaId = acaClassRepository.findAcaIdByClassId(req.getClassId())
                    .orElse(null);

            if (acaId == null) {
                userMessage.setMessage(" 해당 classId에 속한 학원을 찾을 수 없습니다.");
                return 0;
            }
        }

        // 학원 관계자인지 확인
        boolean isAcademyOwner = academyRepository.isUserOwnerOfAcademy(acaId, requestUserId);
        if (!isAcademyOwner) {
            userMessage.setMessage(" 해당 학원을 관리할 권한이 없습니다.");
            return 0;
        }

        // 리뷰가 해당 수업에 속하는지 검증
        boolean isReviewBelongToClass = reviewRepository.existsByReviewIdAndClassId(req.getReviewId(), req.getClassId());
        if (!isReviewBelongToClass) {
            userMessage.setMessage("해당 수업에 속한 리뷰가 아닙니다.");
            return 0;
        }

        int deletedCount = reviewRepository.deleteByReviewIdAndClassId(req.getReviewId(), req.getClassId());
        userMessage.setMessage("리뷰 삭제가 완료되었습니다.");
        return deletedCount;
    }


//    // 리뷰 삭제 (학원 관계자)
//    @Transactional
//    public int deleteReviewByAcademy(ReviewDelMyacademyReq req) {
//        // 학원 ID(acaId)와 리뷰 ID(reviewId) 검증
//        if (req.getAcaId() == null ) {
//            userMessage.setMessage("학원 ID(acaId)가 누락되었습니다.");
//            return 0;
//        }
//
//        if (req.getReviewId() == null) {
//            userMessage.setMessage("리뷰 ID가 누락되었습니다.");
//            return 0;
//        }
//
//
//
//        long jwtUserId = validateAuthenticatedUser(); // JWT에서 가져온 유저 ID 검증
//        long requestUserId = req.getUserId();
//
//        // 본인 계정 검증
//        if (jwtUserId != requestUserId) {
//            userMessage.setMessage("잘못된 요청입니다. 본인의 계정으로만 학원 리뷰 관리가 가능합니다.");
//            return 0;
//        }
//
//        // 유저 존재 여부 확인
//        validateUserExists(req.getUserId());
//
//        if (!isAuthorizedUser(req.getUserId())) {
//            return 0;
//        }
//
//        Long acaId = req.getAcaId();
//        if (acaId == null) {
//            userMessage.setMessage("학원 ID가 제공되지 않았습니다.");
//            log.error("AcaId is null for userId: {}", requestUserId);
//            return 0;
//        }
//
//        validateUserExists(req.getUserId());
//
//        if (!isAuthorizedUser(req.getUserId())) {
//            return 0;  // 인증되지 않은 요청이면 종료
//        }
//
//        List<Integer> reviewIds = mapper.getReviewIdsByAcaIdAndUser(acaId, requestUserId);
//
//        if (!reviewIds.isEmpty()) {
//            mapper.deleteReviewByReviewId(reviewIds);
//            log.info("학원(acaId: {})에 대한 사용자(userId: {}) 리뷰 삭제 완료!", acaId, requestUserId);
//        } else {
//            log.warn("삭제할 리뷰가 없습니다. reviewId가 NULL입니다.");
//        }
//
//        if (mapper.checkAcaExists(acaId) == 0) {
//            userMessage.setMessage("유효하지 않은 학원 ID입니다.");
//            return 0;
//        }
//
//        if (mapper.checkUserExists(req.getUserId()) == 0) {
//            userMessage.setMessage("유효하지 않은 유저 ID입니다.");
//            return 0;
//        }
//
//        if (mapper.checkReviewExists(req.getReviewId()) == 0) {
//            userMessage.setMessage("유효하지 않은 리뷰 ID입니다.");
//            return 0;
//        }
//
//        // 학원 관계자 권한 확인
//        if (!isUserLinkedToAcademy(req.getAcaId(), req.getUserId())) {
//            userMessage.setMessage("해당 학원을 관리할 권한이 없습니다.");
//            return 0;
//        }
//
//        // 리뷰 존재 여부 확인
//        if (mapper.checkReviewExists(req.getReviewId()) == 0) {
//            userMessage.setMessage("삭제할 리뷰를 찾을 수 없습니다.");
//            return 0;
//        }
//
//        // 삭제할 리뷰의 학원 ID 조회
//        Long reviewAcaId = mapper.findAcademyIdByReviewId(req.getReviewId());
//        if (reviewAcaId == null) {
//            userMessage.setMessage("삭제할 리뷰를 찾을 수 없습니다.");
//            return 0;
//        }
//
//        // 관리자가 해당 학원과 관련이 있는지 검증
//        Integer isAdminOfAcademy = mapper.isUserLinkedToAcademy(reviewAcaId, requestUserId);
//        if (isAdminOfAcademy == null || isAdminOfAcademy == 0) {
//            userMessage.setMessage("삭제할 리뷰는 로그인한 관리자의 학원과 관련이 없습니다.");
//            return 0;
//        }
//
//        // 해당 리뷰가 해당 학원에 속하는지 확인
//        if (!isReviewLinkedToAcademy(req.getReviewId(), req.getAcaId())) {
//            userMessage.setMessage("해당 리뷰는 요청한 학원에 속해 있지 않습니다.");
//            return 0;
//        }
//
//        // 리뷰 삭제 수행
//        int rowsDeleted = mapper.deleteReviewByAcademy(req);
//        if (rowsDeleted == 0) {
//            userMessage.setMessage("삭제할 리뷰를 찾을 수 없습니다.");
//            return 0;
//        }
//
//        // 리뷰 삭제 완료 메시지
//        userMessage.setMessage("리뷰 삭제가 완료되었습니다.");
//        return 1;
//    }




    // 학원 관리자의 자신의 모든 학원 리뷰 조회 (로그인 필요)
    @Transactional
    public List<ReviewDto> getMyAcademyReviews(MyAcademyReviewListGetReq req) {

        long jwtUserId = validateAuthenticatedUser();
        long requestUserId = req.getUserId();

        // 본인 계정 검증
        if (jwtUserId != requestUserId) {
            userMessage.setMessage("잘못된 요청입니다. 본인의 계정으로만 본인의 학원들의 리뷰 리스트를 조회할 수 있습니다.");
            return Collections.emptyList();
        }

        // 유저 존재 여부 확인
        validateUserExists(req.getUserId());

        if (!isAuthorizedUser(req.getUserId())) {
            return Collections.emptyList();  //  인증되지 않은 요청이면 바로 종료
        }

        validateAuthenticatedUser(req.getUserId());

        if (mapper.checkUserExists(req.getUserId()) == 0) {
            userMessage.setMessage("유효하지 않은 유저 ID입니다.");
            return Collections.emptyList();
        }

        // 학원 관계자 권한 검증
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
            userMessage.setMessage("잘못된 요청입니다. 본인의 계정으로만 작성한 리뷰 리스트를 볼 수 있습니다.");
            return Collections.emptyList();
        }
        validateUserExists(req.getUserId());

        if (mapper.checkUserExists(req.getUserId()) == 0) {
            userMessage.setMessage("유효하지 않은 유저 ID입니다.");
            return Collections.emptyList();
        }

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

    // 공개 학원 리뷰 조회 (로그인 필요 없음)

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
        long jwtUserId = AuthenticationFacade.getSignedUserId();

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


//    private boolean validateReviewRequest(ReviewPostReq req) {
//        // 1. 수업 참여 ID 조회 (classId + userId 기반으로 조회)
//        List<Long> joinClassId = mapper.findJoinClassIdByAcademyAndUser(req.getAcaId(), req.getUserId());
//
//
//        if (joinClassId == null) {
//            userMessage.setMessage("해당 수업에 등록된 기록이 없습니다.");
//            return false;
//        }
//
//        // 2. 사용자가 수업을 정상적으로 수료했는지 확인
//        if (mapper.checkEnrollment(req.getClassId(), req.getUserId()) == 0) {
//            userMessage.setMessage("수업에 참여한 사용자만 리뷰를 작성할 수 있습니다.");
//            return false;
//        }
//
//        // 3. 별점 유효성 검사
//        if (req.getStar() < 1 || req.getStar() > 5) {
//            userMessage.setMessage("별점은 1~5 사이의 값이어야 합니다.");
//            return false;
//        }
//
//        // 4. 리뷰 내용 검증 (빈 문자열 허용)
//        if (req.getComment() == null || req.getComment().trim().isEmpty()) {
//            req.setComment(""); // 빈 문자열로 설정
//        }
//
//        if (req.getReviewId() != null) {
//            userMessage.setMessage("");
//            return false;
//        }
//        return true;
//    }
//
//    private boolean validateReviewRequest2(ReviewUpdateReq req) {
//        if (req.getAcaId() == null || req.getAcaId() <= 0 || mapper.isValidJoinClassId(req.getJoinClassId()) == 0) {
//            userMessage.setMessage("유효하지 않은 수업 참여 ID입니다.");
//            return false;
//        }
//        if (mapper.checkEnrollment(req.getAcaId(), req.getUserId()) == 0) {
//            userMessage.setMessage("수업에 참여한 사용자만 리뷰를 작성할 수 있습니다.");
//            return false;
//        }
//        if (req.getStar() < 1 || req.getStar() > 5) {
//            userMessage.setMessage("별점은 1~5 사이의 값이어야 합니다.");
//            return false;
//        }
//        if (req.getComment() == null || req.getComment().trim().isEmpty()) {
//            req.setComment(""); // 빈 문자열로 설정
//        }
//        return true;
//    }

//    private boolean validateReviewRequest3(ReviewDelReq req) {
//
//        // 해당 사용자가 수업을 수료했는지 확인 (필요 시 추가)
//        if (mapper.checkEnrollment(req.getClassId(), req.getUserId()) == 0) {
//            userMessage.setMessage("수업을 수료한 사용자만 리뷰를 삭제할 수 있습니다.");
//            return false;
//        }
//
//        return true;
//    }
//

    private void validateAcademy(long acaId) {
        if (acaId <= 0 || mapper.checkAcaExists(acaId) == 0) {
            throw new CustomException(ReviewErrorCode.INVALID_ACADEMY);
        }
    }

    /**
     * 리뷰 작성자인지 검증
     */
    private void validateReviewAuthor(List<Integer> reviewIds, long userId) {
        if (!isUserAuthorOfReview(reviewIds, userId)) {
            throw new CustomException(ReviewErrorCode.UNRIGHT_USER);
        }
    }

    /**
     * 해당 유저가 리뷰 작성자인지 확인
     */
    private boolean isUserAuthorOfReview(List<Integer> reviewIds, long userId) {
        Integer isAuthor = mapper.isUserAuthorOfReview(reviewIds, userId);
        return isAuthor != null && isAuthor > 0;
    }

    /**
     * 학원 관계자 권한 검증
     */
    private void checkUserAcademyOwnership(long acaId, long userId) {
        if (!isUserLinkedToAcademy(acaId, userId)) {
            throw new CustomException(ReviewErrorCode.UNAUTHORIZED_ACADEMY_ACCESS);
        }
    }

    /**
     * 유저가 특정 학원과 연관되어 있는지 확인
     */
    private boolean isUserLinkedToAcademy(long acaId, long userId) {
        Integer count = mapper.isUserLinkedToAcademy(acaId, userId);
        return count != null && count > 0;
    }


    /**
     * 리뷰가 특정 학원에 속하는지 확인
     */
    private boolean isReviewLinkedToAcademy(long joinClassId, long acaId) {
        Long count = mapper.isReviewLinkedToAcademy(joinClassId, acaId);
        return count != null && count > 0;
    }


    public boolean isAdmin(Long userId) {
        return userRepository.findById(userId)
                .map(user -> user.getUserRole().isAdmin()) // UserRole의 isAdmin() 활용
                .orElse(false);
    }



}
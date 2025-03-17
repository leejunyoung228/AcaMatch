package com.green.acamatch.review;

import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.config.exception.AcaClassErrorCode;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.ReviewErrorCode;
import com.green.acamatch.config.exception.UserErrorCode;
import com.green.acamatch.entity.joinClass.JoinClass;
import com.green.acamatch.entity.review.Review;
import com.green.acamatch.entity.review.ReviewPic;
import com.green.acamatch.entity.review.ReviewPicIds;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.joinClass.JoinClassRepository;
import com.green.acamatch.review.model.ReviewPostReq;
import com.green.acamatch.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewMapper reviewMapper;
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
        log.debug("Principal 정보: {}", principal.getClass().getName());  // 타입 확인 로그 추가

        if (principal instanceof JwtUser) {
            log.debug("JWT에서 추출한 userId: {}", ((JwtUser) principal).getSignedUserId());
            return ((JwtUser) principal).getSignedUserId();
        } else if (principal instanceof CustomUserDetails) {
            log.debug("CustomUserDetails에서 추출한 userId: {}", ((CustomUserDetails) principal).getUserId());
            return ((CustomUserDetails) principal).getUserId();
        } else if (principal instanceof String) {
            try {
                return Long.parseLong((String) principal); // String인 경우 숫자로 변환
            } catch (NumberFormatException e) {
                log.error("String principal을 Long으로 변환하는 중 오류 발생: {}", principal);
                throw new CustomException(UserErrorCode.USER_NOT_FOUND);
            }
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
    public Integer postReview(List<MultipartFile> pics, ReviewPostReq p) {
        JoinClass joinClass = joinClassRepository.findById(p.getJoinClassId()).orElseThrow(()
                -> new CustomException(AcaClassErrorCode.NOT_FOUND_JOIN_CLASS));
        User user = userRepository.findById(p.getUserId()).orElseThrow(()
                -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        Review review = new Review();
        review.setJoinClass(joinClass);
        review.setUser(user);
        review.setComment(p.getComment());
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());
        review.setStar(p.getStar());
        review.setBanReview(p.getBanReview());

        reviewRepository.save(review);

        long reviewId = review.getReviewId();
        String middlePath = String.format("review/%d", reviewId);
        myFileUtils.makeFolders(middlePath);

        List<String> picNameList = new ArrayList<>();

        for (MultipartFile pic : pics) {
            if (pic != null && !pic.isEmpty()) {
                String savedPicName = myFileUtils.makeRandomFileName(pic);
                String filePath = String.format("%s/%s", middlePath, savedPicName);
                picNameList.add(savedPicName);

                try {
                    myFileUtils.transferTo(pic, filePath);

                    ReviewPicIds reviewPicIds = new ReviewPicIds();
                    reviewPicIds.setReviewId(reviewId);
                    reviewPicIds.setReviewPic(savedPicName);

                    ReviewPic reviewPic = new ReviewPic();
                    reviewPic.setReviewPicIds(reviewPicIds);
                    reviewPic.setReview(review);

                    reviewPicRepository.save(reviewPic);
                } catch (IOException e) {
                    myFileUtils.deleteFolder(middlePath, true);
                    log.error("파일 저장 실패: " + e.getMessage());
                    throw new CustomException(ReviewErrorCode.IMAGE_UPLOAD_FAILED);
                }
            }
        } return 1;
    }
}




//
//
//    // 공개 학원 리뷰 조회 (로그인 필요 없음)
//
//    @Transactional
//    public List<ReviewDto> getAcademyReviewsForPublic(ReviewListGetReq req) {
//        validateAcademy(req.getAcaId());
//
//        List<ReviewDto> reviews = mapper.getAcademyReviewsForPublic(req);
//        if (reviews.isEmpty()) {
//            userMessage.setMessage("리뷰가 존재하지 않습니다.");
//            return Collections.emptyList();
//        }
//
//        userMessage.setMessage("리뷰 조회가 완료되었습니다.");
//        return reviews;
//    }


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
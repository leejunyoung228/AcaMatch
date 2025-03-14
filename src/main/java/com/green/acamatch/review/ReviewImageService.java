package com.green.acamatch.review;

import com.green.acamatch.acaClass.ClassRepository;
import com.green.acamatch.config.FilePathConfig;
import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.config.exception.*;
import com.green.acamatch.config.security.AuthenticationFacade;
import com.green.acamatch.entity.joinClass.JoinClass;
import com.green.acamatch.entity.myenum.UserRole;
import com.green.acamatch.entity.review.Review;
import com.green.acamatch.entity.review.ReviewPic;
import com.green.acamatch.entity.review.ReviewPicIds;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.joinClass.JoinClassRepository;
import com.green.acamatch.review.dto.MyReviewDto;
import com.green.acamatch.review.dto.ReviewDto;
import com.green.acamatch.review.model.*;
import com.green.acamatch.user.repository.RelationshipRepository;
import com.green.acamatch.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;


@Service
@RequiredArgsConstructor
public class ReviewImageService {

    private final ReviewMapper mapper;
    private final UserMessage userMessage;
    private static final Logger log = LoggerFactory.getLogger(ReviewImageService.class);
    private final RelationshipRepository relationshipRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final JoinClassRepository joinClassRepository;
    private final ClassRepository acaClassRepository;
    private final ReviewPicRepository reviewPicRepository;
    private final MyFileUtils myFileUtils;
    private final FilePathConfig filePathConfig;

//    // 모든 필드를 포함 생성자
//    public ReviewImageService(
//            ReviewMapper mapper,
//            UserMessage userMessage,
//            RelationshipRepository relationshipRepository,
//            ReviewRepository reviewRepository,
//            UserRepository userRepository,
//            JoinClassRepository joinClassRepository,
//            ClassRepository acaClassRepository,
//            ReviewPicRepository reviewPicRepository,
//            MyFileUtils myFileUtils,
//            FilePathConfig filePathConfig) {
//        this.mapper = mapper;
//        this.userMessage = userMessage;
//        this.relationshipRepository = relationshipRepository;
//        this.reviewRepository = reviewRepository;
//        this.userRepository = userRepository;
//        this.joinClassRepository = joinClassRepository;
//        this.acaClassRepository = acaClassRepository;
//        this.reviewPicRepository = reviewPicRepository;
//        this.myFileUtils = myFileUtils;
//        this.filePathConfig = filePathConfig;
//    }

    /**
     * 리뷰 서비스에서 로그인된 사용자 검증
     */
    private long validateAuthenticatedUser(long requestUserId) {
        long jwtUserId = AuthenticationFacade.getSignedUserId();

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
    public int createReview(@Valid ReviewPostReqForParent req, List<MultipartFile> files) {
        long jwtUserId = validateAuthenticatedUser();  // JWT 토큰에서 인증된 사용자 ID 가져오기

        // 사용자 ID 검증
        Long requestUserId = req.getUserId();
        if (requestUserId == null || requestUserId == 0L) {
            userMessage.setMessage("잘못된 요청입니다. 유효한 사용자 ID가 필요합니다.");
            return 0;
        }
        if (jwtUserId != requestUserId) {
            userMessage.setMessage("잘못된 요청입니다. 본인의 계정으로만 리뷰를 등록할 수 있습니다.");
            return 0;
        }

        // 유효한 학생 또는 학부모인지 검증
        List<UserRole> validRoles = Arrays.asList(UserRole.STUDENT, UserRole.PARENT);
        boolean isValidUser = userRepository.existsByUserIdAndUserRoleIn(requestUserId, validRoles);
        if (!isValidUser) {
            userMessage.setMessage("리뷰를 작성할 권한이 없습니다. 학생 또는 학부모 계정으로 로그인해주세요.");
            return 0;
        }

        // 해당 유저가 실제로 해당 수업을 수강했는지 검증
        JoinClass joinClass = joinClassRepository.findByAcaClass_ClassIdAndUser_UserId(req.getClassId(), requestUserId)
                .stream()
                .findFirst()
                .orElseThrow(() -> {
                    userMessage.setMessage("해당 학원의 수업을 수강한 기록이 없습니다. 수강한 후 리뷰를 작성할 수 있습니다.");
                    return new CustomException(ReviewErrorCode.CLASS_NOT_FOUND);
                });
        // 중복 리뷰 작성 방지
        if (reviewRepository.existsByJoinClass(joinClass)) {
            userMessage.setMessage("이미 해당 학원에 대한 리뷰를 작성하셨습니다.");
            return 0;
        }

        // 사용자 정보 조회
        User reviewWriter = joinClass.getUser(); //`join_class.user_id` 강제 적용
        if (reviewWriter == null) {
            userMessage.setMessage("유효하지 않은 사용자입니다.");
            return 0;
        }

        // 별점 범위 검증
        if (req.getStar() < 1 || req.getStar() > 5) {
            userMessage.setMessage("별점은 1~5 사이의 값이어야 합니다.");
            return 0;
        }

        // 댓글 기본값 처리
        String comment = req.getComment() == null ? "" : req.getComment().trim();

        // 리뷰 저장
        Review newReview = new Review();
        newReview.setUser(reviewWriter);  //`join_class.user_id`를 강제로 사용
        newReview.setJoinClass(joinClass);
        newReview.setComment(comment);
        newReview.setStar(req.getStar());
        newReview.setBanReview(0); // 기본값 0 설정

        reviewRepository.save(newReview);

        //  미디어 파일 저장
        if (files != null && !files.isEmpty()) {
            saveReviewFiles(newReview, files);
        }

        userMessage.setMessage("리뷰가 성공적으로 등록되었습니다.");
        return 1;
    }



    @Transactional
    public int updateReviewWithFiles(@Valid ReviewUpdateReq req, List<MultipartFile> files, List<String> deletedFiles) {
        long jwtUserId = validateAuthenticatedUser();
        long requestUserId = req.getUserId();

        if (jwtUserId != requestUserId) {
            userMessage.setMessage("잘못된 요청입니다. 본인의 계정으로만 리뷰를 수정할 수 있습니다.");
            return 0;
        }

        try {
            // 기존 리뷰 조회
            Review existingReview = reviewRepository.findById(req.getReviewId())
                    .orElseThrow(() -> new CustomException(ReviewErrorCode.INVALID_REVIEW_DATA));

            // 리뷰 작성자 검증
            if (!existingReview.getUser().getUserId().equals(requestUserId)) {
                userMessage.setMessage("본인의 리뷰만 수정할 수 있습니다.");
                return 0;
            }

            // 리뷰 정보 업데이트 (별점, 코멘트 수정 반영)
            boolean isUpdated = false;

            // 별점 값이 null이면 기본값(기존 값 유지)
            Double newStar = req.getStar() != null ? req.getStar() : existingReview.getStar();

            if (!Objects.equals(existingReview.getStar(), newStar)) {
                existingReview.setStar(newStar); // 별점 업데이트
                isUpdated = true;
            }

            if (!Objects.equals(existingReview.getComment(), req.getComment())) {
                existingReview.setComment(req.getComment()); // 코멘트 업데이트
                isUpdated = true;
            }

            // 변경 사항이 있으면 저장
            if (isUpdated) {
                reviewRepository.save(existingReview);
                reviewRepository.flush(); // 강제 반영 (필요한 경우)
                log.info("리뷰 내용(별점, 코멘트) 업데이트 완료!");
            }

            // 기존 파일 삭제 처리
            List<String> failedToDelete = deleteReviewFiles(existingReview.getReviewId(), deletedFiles);
            if (!failedToDelete.isEmpty()) {
                userMessage.setMessage("일부 파일 삭제에 실패했습니다: " + failedToDelete);
                return 0;
            }

            // 최소 1개 사진 유지 검사
            long remainingImageCount = reviewPicRepository.countByReviewPicIds_ReviewId(existingReview.getReviewId());
            if (remainingImageCount == 0 && (files == null || files.isEmpty())) {
                userMessage.setMessage("이미지 리뷰에는 최소 1개의 사진이 필요합니다.");
                return 0;
            }

            // 새로운 파일 추가
            if (files != null && !files.isEmpty()) {
                boolean uploadSuccess = saveNewReviewFiles(existingReview, files);
                if (!uploadSuccess) {
                    userMessage.setMessage("리뷰 수정 중 파일 업로드에 실패했습니다.");
                    return 0;
                }
            }

            // 리뷰 수정 성공 메시지
            userMessage.setMessage("리뷰 수정이 완료되었습니다.");
            return 1;

        } catch (CustomException e) {
            userMessage.setMessage(e.getMessage());
            return 0;
        } catch (Exception e) {
            log.error("리뷰 수정 중 오류 발생: ", e);
            userMessage.setMessage("리뷰 수정 중 오류가 발생했습니다: " + e.getMessage());
            return 0;
        }
    }




    private List<String> deleteReviewFiles(long reviewId, List<String> deletedFiles) {
        List<String> failedToDelete = new ArrayList<>();

        if (deletedFiles == null || deletedFiles.isEmpty()) {
            return failedToDelete;
        }

        String basePath = filePathConfig.getUploadDir(); // 중복 경로 문제 확인

        for (String fileName : deletedFiles) {
            Optional<ReviewPic> reviewPicOpt = reviewPicRepository
                    .findByReviewPicIds_ReviewIdAndReviewPicIds_ReviewPic(reviewId, fileName);

            if (reviewPicOpt.isPresent()) {
                String fileCategory = fileName.endsWith(".mp4") || fileName.endsWith(".mov") ? "videos" : "images";
                String fullPath = Paths.get(basePath, fileCategory, fileName).toString();

                File file = new File(fullPath);

                // 파일이 존재하지 않으면 DB에서만 삭제 (실제 파일이 없을 가능성 있음)
                if (!file.exists()) {
                    log.warn("파일이 존재하지 않음: {}", fullPath);
                    reviewPicRepository.deleteByReviewPicIds_ReviewIdAndReviewPicIds_ReviewPic(reviewId, fileName);
                    continue;
                }

                // 파일 삭제 시도
                if (!file.delete()) {
                    log.error("파일 삭제 실패: {}", fullPath);
                    failedToDelete.add(fileName);
                } else {
                    reviewPicRepository.deleteByReviewPicIds_ReviewIdAndReviewPicIds_ReviewPic(reviewId, fileName);
                    log.info("파일 삭제 성공: {}", fullPath);
                }
            } else {
                log.warn("삭제할 파일이 DB에 존재하지 않음: {}", fileName);
            }
        }
        return failedToDelete;
    }




    //  리뷰 삭제 (작성자 본인)

    @Transactional
    public int deleteReviewByUser(ReviewDelReq req) {
        // 필수 파라미터 확인
        if (req.getAcaId() == null || req.getUserId() == null) {
            userMessage.setMessage("잘못된 요청입니다. acaId와 userId가 필요합니다.");
            return 0;
        }

        long jwtUserId = validateAuthenticatedUser();
        long requestUserId = req.getUserId();

        // 본인 계정 검증
        if (jwtUserId != requestUserId) {
            userMessage.setMessage("잘못된 요청입니다. 본인의 계정으로만 리뷰를 삭제할 수 있습니다.");
            return 0;
        }

        // 유저 존재 여부 확인
        if (mapper.checkUserExists(requestUserId) == 0) {
            userMessage.setMessage("유효하지 않은 유저 ID입니다.");
            return 0;
        }

        // 학원 존재 여부 확인
        List<Long> classIds = mapper.findClassIdByAcaId(req.getAcaId()); // acaId 기준으로 classId 조회
        log.info(" 학원(acaId: {})에 속한 클래스 ID 리스트: {}", req.getAcaId(), classIds);

        if (classIds.isEmpty()) {
            userMessage.setMessage("해당 학원에 등록된 수업이 없습니다.");
            return 0;
        }

// 올바른 class_id 리스트를 가져와서 JOINCLASS 확인
        int enrollmentCheck = mapper.checkEnrollmentByClassIds(classIds, requestUserId);
        if (enrollmentCheck == 0) {
            userMessage.setMessage("해당 학원의 수업을 수강한 기록이 없습니다.");
            return 0;
        }

        // joinClassId 조회
        List<Long> joinClassIds = mapper.findJoinClassIdByAcademyAndUser(req.getAcaId(), requestUserId);
        log.info("joinClassId 리스트: {}", joinClassIds);

        if (joinClassIds.isEmpty()) {
            userMessage.setMessage("해당 학원에 등록된 기록이 없습니다.");
            return 0;
        }

        // 리뷰 ID 조회
        List<Integer> reviewIds = mapper.getReviewIdsByAcaIdAndUser(req.getAcaId(), requestUserId);
        if (reviewIds.isEmpty()) {
            userMessage.setMessage("삭제할 리뷰가 없습니다.");
            log.warn("삭제할 리뷰가 없습니다. reviewId가 NULL입니다.");
            return 0;
        }

        // 작성자 확인
        if (!reviewIds.isEmpty() && !isUserAuthorOfReview(reviewIds, requestUserId)) {
            userMessage.setMessage("해당 리뷰의 작성자가 아닙니다. 삭제할 권한이 없습니다.");
            return 0;
        }

        // 리뷰 삭제 수행
        int rowsDeleted = mapper.deleteReviewByReviewId(reviewIds);
        if (rowsDeleted == 0) {
            userMessage.setMessage("삭제할 리뷰를 찾을 수 없습니다.");
            return 0;
        }

        log.info("학원(acaId: {})에 대한 사용자(userId: {}) 리뷰 삭제 완료!", req.getAcaId(), requestUserId);
        userMessage.setMessage("리뷰 삭제가 완료되었습니다.");
        return 1;
    }


    /**
     * 리뷰 삭제 (학원 관계자)
     */
    @Transactional
    public int deleteReviewByAcademy(ReviewDelMyacademyReq req) {
        // 학원 ID(acaId)와 리뷰 ID(reviewId) 검증
        if (req.getAcaId() == null ) {
            userMessage.setMessage("학원 ID(acaId)가 누락되었습니다.");
            return 0;
        }

        if (req.getReviewId() == null) {
            userMessage.setMessage("리뷰 ID가 누락되었습니다.");
            return 0;
        }



        long jwtUserId = validateAuthenticatedUser(); // JWT에서 가져온 유저 ID 검증
        long requestUserId = req.getUserId();

        // 본인 계정 검증
        if (jwtUserId != requestUserId) {
            userMessage.setMessage("잘못된 요청입니다. 본인의 계정으로만 학원 리뷰 관리가 가능합니다.");
            return 0;
        }

        // 유저 존재 여부 확인
        validateUserExists(req.getUserId());

        if (!isAuthorizedUser(req.getUserId())) {
            return 0;
        }

        Long acaId = req.getAcaId();
        if (acaId == null) {
            userMessage.setMessage("학원 ID가 제공되지 않았습니다.");
            log.error("AcaId is null for userId: {}", requestUserId);
            return 0;
        }


        validateUserExists(req.getUserId());

        if (!isAuthorizedUser(req.getUserId())) {
            return 0;  // 인증되지 않은 요청이면 종료
        }

        List<Integer> reviewIds = mapper.getReviewIdsByAcaIdAndUser(acaId, requestUserId);

        if (!reviewIds.isEmpty()) {
            mapper.deleteReviewByReviewId(reviewIds);
            log.info(" 학원(acaId: {})에 대한 사용자(userId: {}) 리뷰 삭제 완료!", acaId, requestUserId);
        } else {
            log.warn(" 삭제할 리뷰가 없습니다. reviewId가 NULL입니다.");
        }

        if (mapper.checkAcaExists(acaId) == 0) {
            userMessage.setMessage("유효하지 않은 학원 ID입니다.");
            return 0;
        }

        if (mapper.checkUserExists(req.getUserId()) == 0) {
            userMessage.setMessage("유효하지 않은 유저 ID입니다.");
            return 0;
        }

        if (mapper.checkReviewExists(req.getReviewId()) == 0) {
            userMessage.setMessage("유효하지 않은 리뷰 ID입니다.");
            return 0;
        }




        // 학원 관계자 권한 확인
        if (!isUserLinkedToAcademy(req.getAcaId(), req.getUserId())) {
            userMessage.setMessage("해당 학원을 관리할 권한이 없습니다.");
            return 0;
        }

        // 리뷰 존재 여부 확인
        if (mapper.checkReviewExists(req.getReviewId()) == 0) {
            userMessage.setMessage("삭제할 리뷰를 찾을 수 없습니다.");
            return 0;
        }

        // 삭제할 리뷰의 학원 ID 조회
        Long reviewAcaId = mapper.findAcademyIdByReviewId(req.getReviewId());
        if (reviewAcaId == null) {
            userMessage.setMessage("삭제할 리뷰를 찾을 수 없습니다.");
            return 0;
        }

        // 관리자가 해당 학원과 관련이 있는지 검증
        Integer isAdminOfAcademy = mapper.isUserLinkedToAcademy(reviewAcaId, requestUserId);
        if (isAdminOfAcademy == null || isAdminOfAcademy == 0) {
            userMessage.setMessage("삭제할 리뷰는 로그인한 관리자의 학원과 관련이 없습니다.");
            return 0;
        }

        // 해당 리뷰가 해당 학원에 속하는지 확인
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
        public List<ReviewDto> getMyAcademyReviews (MyAcademyReviewListGetReq req){

            long jwtUserId = validateAuthenticatedUser();
            long requestUserId = req.getUserId();

            // 본인 계정 검증
            if (jwtUserId != requestUserId) {
                userMessage.setMessage("잘못된 요청입니다. 본인의 계정으로만 본인의 학원들의 리뷰 리스트를 조회할 수 있습니다.");
                return Collections.emptyList();
            }

            //  유저 존재 여부 확인 (추가)
            validateUserExists(req.getUserId());
            if (!isAuthorizedUser(req.getUserId())) {
                return Collections.emptyList();  //  인증되지 않은 요청이면 바로 종료
            }

            validateAuthenticatedUser(req.getUserId());

            if (mapper.checkUserExists(req.getUserId()) == 0) {
                userMessage.setMessage("유효하지 않은 유저 ID입니다.");
                return Collections.emptyList();
            }


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
        public List<MyReviewDto> getReviewsByUserId (MyMediaReviewGetReq req){
            long jwtUserId = validateAuthenticatedUser(); // JWT에서 가져온 유저 ID 검증
            long requestUserId = req.getSignedUserId();

            // 1. 본인 계정 검증
            if (jwtUserId != requestUserId) {
                userMessage.setMessage("잘못된 요청입니다. 본인의 계정으로만 작성한 리뷰 리스트를 볼 수 있습니다.");
                return Collections.emptyList();
            }
            validateUserExists(req.getSignedUserId());

            if (mapper.checkUserExists(req.getSignedUserId()) == 0) {
                userMessage.setMessage("유효하지 않은 유저 ID입니다.");
                return Collections.emptyList();
            }

            if (!isAuthorizedUser(req.getSignedUserId())) {
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
        public List<ReviewDto> getAcademyReviewsForPublic (ReviewListGetReq req){
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
        private long validateAuthenticatedUser () {
            long jwtUserId = AuthenticationFacade.getSignedUserId();

            //  유저 ID가 0이면 예외 처리 (잘못된 토큰이거나 요청)
            if (jwtUserId == 0) {
                throw new CustomException(ReviewErrorCode.INVALID_USER);
            }

            return jwtUserId;
        }


        /**
         * 사용자 ID가 DB에 존재하는지 확인
         */
        private void validateUserExists ( long userId){
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

        private void validateAcademy ( long acaId){
            if (acaId <= 0 || mapper.checkAcaExists(acaId) == 0) {
                throw new CustomException(ReviewErrorCode.INVALID_ACADEMY);
            }
        }

        /**  리뷰 작성자인지 검증 */
        private void validateReviewAuthor ( List<Integer> reviewIds, long userId){
            if (!isUserAuthorOfReview(reviewIds, userId)) {
                throw new CustomException(ReviewErrorCode.UNRIGHT_USER);
            }
        }

        /**  해당 유저가 리뷰 작성자인지 확인 */
        private boolean isUserAuthorOfReview (List<Integer> reviewIds, long userId){
            Integer isAuthor = mapper.isUserAuthorOfReview(reviewIds, userId);
            return isAuthor != null && isAuthor > 0;
        }

        /**  학원 관계자 권한 검증 */
        private void checkUserAcademyOwnership ( long acaId, long userId){
            if (!isUserLinkedToAcademy(acaId, userId)) {
                throw new CustomException(ReviewErrorCode.UNAUTHORIZED_ACADEMY_ACCESS);
            }
        }

        /** 유저가 특정 학원과 연관되어 있는지 확인 */
        private boolean isUserLinkedToAcademy ( long acaId, long userId){
            Integer count = mapper.isUserLinkedToAcademy(acaId, userId);
            return count != null && count > 0;
        }


        /** 리뷰가 특정 학원에 속하는지 확인 */
        private boolean isReviewLinkedToAcademy ( long joinClassId, long acaId){
            Long count = mapper.isReviewLinkedToAcademy(joinClassId, acaId);
            return count != null && count > 0;
        }



    // 미디어 파일 저장
    private void saveReviewFiles(Review newReview, List<MultipartFile> files) {
        String middlePath = String.format("reviews/%s", newReview.getReviewId()); // 리뷰 ID 포함한 폴더 구조 유지

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                continue; // 빈 파일 무시
            }

            // 파일 유형 구분
            String fileType = file.getContentType();
            if (fileType == null) {
                throw new CustomException(CommonErrorCode.INVALID_PARAMETER);
            }

            String fileCategory = fileType.startsWith("image") ? "images" : "videos";
            String filePath = String.format("%s/%s/", middlePath, fileCategory);
            myFileUtils.makeFolders(filePath);

            // 파일 저장
            String savedFileName = myFileUtils.makeRandomFileName(file);

            // 혹시라도 전체 경로가 저장될 수 있으므로 파일명만 강제 추출
            savedFileName = Paths.get(savedFileName).getFileName().toString();
            if (savedFileName.contains("/")) {
                savedFileName = savedFileName.substring(savedFileName.lastIndexOf("/") + 1);
            }
            if (savedFileName.contains("\\")) {
                savedFileName = savedFileName.substring(savedFileName.lastIndexOf("\\") + 1);
            }

            String fullPath = filePath + savedFileName;

            try {
                myFileUtils.transferTo(file, fullPath); // 실제 파일 저장
            } catch (IOException e) {
                String delFolderPath = String.format("%s/%s", myFileUtils.getUploadPath(), middlePath);
                myFileUtils.deleteFolder(delFolderPath, true);
                throw new CustomException(CommonErrorCode.FILE_UPLOAD_FAILED);
            }

            // ReviewPic 저장 (파일명만 저장)
            ReviewPic reviewPic = new ReviewPic();
            ReviewPicIds reviewPicIds = new ReviewPicIds();
            reviewPicIds.setReviewId(newReview.getReviewId());

            log.debug("최종 저장되는 파일명 (DB에 저장될 값): {}", savedFileName); // 확인용 로그

            reviewPicIds.setReviewPic(savedFileName); // 파일명만 저장
            reviewPic.setReviewPicIds(reviewPicIds);
            reviewPic.setReview(newReview);

            reviewPicRepository.save(reviewPic);
        }
    }


    // 새로운 리뷰 파일 저장 (중복 방지 포함)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean saveNewReviewFiles(Review review, List<MultipartFile> files) {
        List<String> existingFileNames = reviewPicRepository.findFilePathsByReview(review.getReviewId());

        if (files == null || files.isEmpty()) {
            log.info("새로운 파일이 없으므로 기존 파일 유지");
            return true;
        }

        String baseUploadDir = filePathConfig.getUploadDir();
        log.info("파일 업로드 기본 디렉터리: {}", baseUploadDir);

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                continue;
            }

            String fileType = file.getContentType();
            if (fileType == null) {
                log.warn("유효하지 않은 파일 형식");
                return false;
            }

            String fileCategory = fileType.startsWith("image") ? "images" : "videos";

            // 경로 중복 제거
            String fullSavePath = new File(baseUploadDir, Paths.get(fileCategory, myFileUtils.makeRandomFileName(file)).toString()).getAbsolutePath();
            log.info("최종 저장 경로: {}", fullSavePath);

            try {
                myFileUtils.transferTo(file, fullSavePath);

                ReviewPic reviewPic = new ReviewPic();
                ReviewPicIds reviewPicIds = new ReviewPicIds();
                reviewPicIds.setReviewId(review.getReviewId());

                // 파일명만 저장하도록 수정
                reviewPicIds.setReviewPic(new File(fullSavePath).getName());
                reviewPic.setReviewPicIds(reviewPicIds);
                reviewPic.setReview(review);
                reviewPicRepository.save(reviewPic);

            } catch (IOException e) {
                log.error("파일 업로드 실패: {}", e.getMessage());
                return false;
            }
        }
        return true;
    }


}
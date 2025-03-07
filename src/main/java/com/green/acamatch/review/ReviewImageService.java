package com.green.acamatch.review;

import com.green.acamatch.acaClass.ClassRepository;
import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.config.exception.*;
import com.green.acamatch.config.security.AuthenticationFacade;
import com.green.acamatch.entity.acaClass.AcaClass;
import com.green.acamatch.entity.joinClass.JoinClass;
import com.green.acamatch.entity.myenum.UserRole;
import com.green.acamatch.entity.review.Review;
import com.green.acamatch.entity.review.ReviewPic;
import com.green.acamatch.entity.review.ReviewPicIds;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public int createReview(ReviewPostReqForParent req, List<MultipartFile> files) {
        long jwtUserId = validateAuthenticatedUser();
        Long requestUserId = req.getUserId();

        // 본인 계정 검증
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

        Long targetUserId = requestUserId;

        // 기존 리뷰 확인
        if (reviewRepository.existsByJoinClass_AcaClass_ClassIdAndUser_UserId(req.getClassId(), targetUserId)) {
            userMessage.setMessage("이미 해당 학원에 대한 리뷰를 작성하셨습니다.");
            return 0;
        }

        // JoinClass 조회 (사용자가 수업을 수강했는지 확인)
        JoinClass joinClass = joinClassRepository.findByAcaClass_ClassIdAndUser_UserId(req.getClassId(), targetUserId)
                .orElse(null);

        if (joinClass == null) {
            userMessage.setMessage("해당 학원의 수업을 수강한 기록이 없습니다. 수강한 후 리뷰를 작성할 수 있습니다.");
            return 0;
        }

        // 사용자 정보 조회
        User reviewWriter = userRepository.findById(targetUserId).orElse(null);
        if (reviewWriter == null) {
            userMessage.setMessage("유효하지 않은 사용자입니다.");
            return 0;
        }

        // 별점 범위 검증
        if (req.getStar() < 1 || req.getStar() > 5) {
            userMessage.setMessage("별점은 1~5 사이의 값이어야 합니다.");
            return 0;
        }

        // 댓글이 없을 경우 빈 문자열로 설정
        if (req.getComment() == null || req.getComment().trim().isEmpty()) {
            req.setComment("");
        }

        // 리뷰 저장
        Review newReview = new Review();
        newReview.setUser(reviewWriter);
        newReview.setJoinClass(joinClass);
        newReview.setComment(req.getComment());
        newReview.setStar(req.getStar());
        newReview.setBanReview(0); // 기본값 0 설정

        int rowsInserted = reviewRepository.save(newReview) != null ? 1 : 0;
        if (rowsInserted == 0) {
            userMessage.setMessage("리뷰 등록에 실패했습니다.");
            return 0;
        }

        // 파일 업로드 처리
        if (files != null && !files.isEmpty()) {
            saveReviewFiles(newReview, files);
        }

        userMessage.setMessage("리뷰가 성공적으로 등록되었습니다.");
        return 1;
    }



//    @Transactional
//    public int createReview(ReviewPostReqForParent req, List<MultipartFile> files) {
//        long jwtUserId = validateAuthenticatedUser();
//        Long requestUserId = req.getUserId();
//
//        if (requestUserId == null || requestUserId == 0L) {
//            throw new CustomException(CommonErrorCode.INVALID_PARAMETER);
//        }
//
//        if (jwtUserId != requestUserId) {
//            throw new CustomException(ReviewErrorCode.INVALID_USER);
//        }
//
//        validateUserExists(requestUserId);
//
//        if (joinClassRepository.findStudentsByClassId(req.getClassId()).isEmpty()) {
//            throw new CustomException(ReviewErrorCode. STUDENT_NOT_IN_CLASS);
//        }
//
//        if (!userRepository.existsByUserId(requestUserId)) {
//            throw new CustomException(ReviewErrorCode.INVALID_USER);
//        }
//
//        if (reviewRepository.existsByJoinClass_AcaClass_ClassIdAndUser_UserId(req.getClassId(), req.getUserId())) {
//            throw new CustomException(ReviewErrorCode.CONFLICT_REVIEW_ALREADY_EXISTS);
//        }
//
//        User reviewWriter = userRepository.findById(requestUserId)
//                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
//
//        JoinClass joinClass = joinClassRepository.findById(req.getClassId())
//                .orElseThrow(() -> new CustomException(ManagerErrorCode.CLASS_NOT_FOUND));
//
//        if (req.getStar() < 1 || req.getStar() > 5) {
//            throw new CustomException(CommonErrorCode.INVALID_PARAMETER);
//        }
//
//        if (req.getComment() == null || req.getComment().trim().isEmpty()) {
//            req.setComment("");
//        }
//
//        // 리뷰 저장
//        Review newReview = new Review();
//        newReview.setUser(reviewWriter);
//        newReview.setJoinClass(joinClass);
//        newReview.setComment(req.getComment());
//        newReview.setStar(req.getStar());
//        newReview.setBanReview(0); // 기본값 0 설정
//
//        reviewRepository.save(newReview);
//
//        // 파일이 하나도 없을 경우 예외 발생
//        if (files == null || files.isEmpty()) {
//            throw new CustomException(CommonErrorCode.MISSING_REQUIRED_FILED_EXCEPTION);
//        }
//
//        // 파일 저장 로직
//        String middlePath = String.format("reviews/%s", newReview.getReviewId());
//
//        for (MultipartFile file : files) {
//            if (file == null || file.isEmpty()) {
//                throw new CustomException(CommonErrorCode.INVALID_PARAMETER);
//            }
//
//            // 파일 유형 구분
//            String fileType = file.getContentType();
//            if (fileType == null) {
//                throw new CustomException(CommonErrorCode.INVALID_PARAMETER);
//            }
//
//            String fileCategory = fileType.startsWith("image") ? "images" : "videos";
//            String filePath = String.format("%s/%s/", middlePath, fileCategory);
//            myFileUtils.makeFolders(filePath);
//
//            // 파일 저장
//            String savedFileName = myFileUtils.makeRandomFileName(file);
//            String fullPath = filePath + savedFileName;
//
//            try {
//                myFileUtils.transferTo(file, fullPath);
//            } catch (IOException e) {
//                String delFolderPath = String.format("%s/%s", myFileUtils.getUploadPath(), middlePath);
//                myFileUtils.deleteFolder(delFolderPath, true);
//                throw new CustomException(CommonErrorCode.FILE_UPLOAD_FAILED);
//            }
//
//            // ReviewPic 저장 (NULL 방지)
//            ReviewPic reviewPic = new ReviewPic();
//            reviewPic.setReview(newReview);
//            reviewPic.getReviewPicIds().setReviewPic(fullPath);
//
//            reviewPicRepository.save(reviewPic);
//        }
//
//        return 1;
//    }


//    @Transactional
//    public int createReview(ReviewPostReqForParent req, List<MultipartFile> files) {
//        long jwtUserId = validateAuthenticatedUser();
//        Long requestUserId = req.getUserId();
//
//        if (requestUserId == null || requestUserId == 0L) {
//            throw new CustomException(CommonErrorCode.INVALID_PARAMETER);
//        }
//
//        if (jwtUserId != requestUserId) {
//            throw new CustomException(ReviewErrorCode.INVALID_USER);
//        }
//
//        // 학생(1) 또는 부모(2) 검증
//        List<UserRole> validRoles = Arrays.asList(UserRole.STUDENT, UserRole.PARENT);
//        boolean isValidUser = userRepository.existsByUserIdAndUserRoleIn(requestUserId, validRoles);
//
//        if (!isValidUser) {
//            throw new CustomException(ReviewErrorCode.INVALID_USER);
//        }
//
////        User reviewWriter;
//        Long targetUserId = requestUserId; // 기본값: 본인이 직접 작성
//
////        // 부모(`user_role=2`)가 로그인한 경우 보호자 인증 확인 후 `targetUserId` 설정
////        if (userRepository.findRoleByUserId(requestUserId) == UserRole.PARENT) {
////            if (req.getStudentId() == null) {
////                throw new CustomException(ReviewErrorCode.INVALID_USER);
////            }
////
////            // 보호자-학생 관계 검증
////            Relationship relationship = relationshipRepository.findByParentUserIdAndStudentUserIdAndCertification(
////                            requestUserId, req.getStudentId(), req.getCertification())
////                    .orElseThrow(() -> new CustomException(ReviewErrorCode.UNAUTHORIZED_PARENT));
////
////            targetUserId = req.getStudentId();
////        }
////
////        // 부모가 대신 작성하는 경우에도 학생이 수업을 수강한 기록이 있는지 확인
////        if (!joinClassRepository.existsByClassAndStudentOrParent(req.getClassId(), targetUserId)) {
////            throw new CustomException(ReviewErrorCode.STUDENT_NOT_IN_CLASS);
////        }
////
////        // 부모가 대신 작성할 경우에도 `reviewWriter`는 학생으로 설정
////        reviewWriter = userRepository.findById(targetUserId)
////                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
//
//        // 기존 리뷰가 존재하는지 확인
//        if (reviewRepository.existsByJoinClass_AcaClass_ClassIdAndUser_UserId(req.getClassId(), targetUserId)) {
//            throw new CustomException(ReviewErrorCode.CONFLICT_REVIEW_ALREADY_EXISTS);
//        }
//
//        // JoinClass 조회
//        JoinClass joinClass = joinClassRepository.findByAcaClass_ClassIdAndUser_UserId(req.getClassId(), targetUserId)
//                .orElseThrow(() -> new CustomException(ManagerErrorCode.CLASS_NOT_FOUND));
//
//        if (req.getStar() < 1 || req.getStar() > 5) {
//            throw new CustomException(CommonErrorCode.INVALID_PARAMETER);
//        }
//
//        if (req.getComment() == null || req.getComment().trim().isEmpty()) {
//            req.setComment("");
//        }
//
//        // 리뷰 저장
//        Review newReview = new Review();
////        newReview.setUser(reviewWriter);
//        newReview.setJoinClass(joinClass);
//        newReview.setComment(req.getComment());
//        newReview.setStar(req.getStar());
//        newReview.setBanReview(0); // 기본값 0 설정
//
//        reviewRepository.save(newReview);
//
//        // 파일 저장 로직
//        if (files != null && !files.isEmpty()) {
//            String middlePath = String.format("reviews/%s", newReview.getReviewId());
//
//            for (MultipartFile file : files) {
//                if (file == null || file.isEmpty()) {
//                    continue; // 빈 파일은 무시
//                }
//
//                // 파일 유형 구분
//                String fileType = file.getContentType();
//                if (fileType == null) {
//                    throw new CustomException(CommonErrorCode.INVALID_PARAMETER);
//                }
//
//                String fileCategory = fileType.startsWith("image") ? "images" : "videos";
//                String filePath = String.format("%s/%s/", middlePath, fileCategory);
//                myFileUtils.makeFolders(filePath);
//
//                // 파일 저장
//                String savedFileName = myFileUtils.makeRandomFileName(file);
//                String fullPath = filePath + savedFileName;
//
//                try {
//                    myFileUtils.transferTo(file, fullPath);
//                } catch (IOException e) {
//                    String delFolderPath = String.format("%s/%s", myFileUtils.getUploadPath(), middlePath);
//                    myFileUtils.deleteFolder(delFolderPath, true);
//                    throw new CustomException(CommonErrorCode.FILE_UPLOAD_FAILED);
//                }
//
//                // ReviewPic 저장
//                ReviewPic reviewPic = new ReviewPic();
//                ReviewPicIds reviewPicIds = new ReviewPicIds();
//                reviewPicIds.setReviewId(newReview.getReviewId());
//                reviewPicIds.setReviewPic(fullPath);
//                reviewPic.setReviewPicIds(reviewPicIds);
//                reviewPic.setReview(newReview);
//
//                reviewPicRepository.save(reviewPic);
//            }
//        }
//
//        return 1;
//    }




    // 리뷰 수정
    @Transactional
    public int updateReviewWithFiles(@Valid ReviewUpdateReq req, List<MultipartFile> files, List<String> deletedFiles) {
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

            // 학원 등록 여부 검증 (수강 기록 확인)
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

            // 기존 파일 삭제 처리 (삭제 요청이 있는 경우)
            List<String> failedToDelete = new ArrayList<>();
            if (deletedFiles != null && !deletedFiles.isEmpty()) {
                for (String filePath : deletedFiles) {
                    Optional<ReviewPic> reviewPicOpt = reviewPicRepository.findByReviewPicIds_ReviewIdAndReviewPicIds_ReviewPic(
                            existingReview.getReviewId(), filePath);

                    if (reviewPicOpt.isPresent()) {
                        reviewPicRepository.deleteByReviewPicIds_ReviewIdAndReviewPicIds_ReviewPic(
                                existingReview.getReviewId(), filePath);

                        // 실제 파일 삭제 (실패 시 리스트에 추가)
                        boolean deleted = myFileUtils.deleteFile(filePath);
                        if (!deleted) {
                            failedToDelete.add(filePath);
                        }
                    } else {
                        userMessage.setMessage("삭제할 파일을 찾을 수 없습니다: " + filePath);
                        return 0;
                    }
                }
            }

            // 일부 파일 삭제 실패 예외 처리
            if (!failedToDelete.isEmpty()) {
                userMessage.setMessage("일부 파일 삭제에 실패했습니다: " + failedToDelete);
                return 0;
            }

            // 최소 1개 사진 유지 검사 (삭제 후 사진이 없는 경우 방지)
            long remainingImageCount = reviewPicRepository.countByReviewPicIds_ReviewId(existingReview.getReviewId());
            if (remainingImageCount == 0 && (files == null || files.isEmpty())) {
                userMessage.setMessage("이미지 리뷰에는 최소 1개의 사진이 필요합니다.");
                return 0;
            }

            // 새로운 파일 추가 처리 (중복 방지)
            if (files != null && !files.isEmpty()) {
                saveNewReviewFiles(existingReview, files);
            }

            userMessage.setMessage("리뷰 수정이 완료되었습니다.");
            return 1;
        } catch (Exception e) {
            userMessage.setMessage("리뷰 수정 중 오류가 발생했습니다: " + e.getMessage());
            return 0;
        }
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
        log.info("📌 학원(acaId: {})에 속한 클래스 ID 리스트: {}", req.getAcaId(), classIds);

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
        public List<MyReviewDto> getReviewsByUserId (MyReviewGetReq req){
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
        String middlePath = String.format("reviews/%s", newReview.getReviewId());

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
            String fullPath = filePath + savedFileName;

            try {
                myFileUtils.transferTo(file, fullPath);
            } catch (IOException e) {
                String delFolderPath = String.format("%s/%s", myFileUtils.getUploadPath(), middlePath);
                myFileUtils.deleteFolder(delFolderPath, true);
                throw new CustomException(CommonErrorCode.FILE_UPLOAD_FAILED);
            }

            // ReviewPic 저장
            ReviewPic reviewPic = new ReviewPic();
            ReviewPicIds reviewPicIds = new ReviewPicIds();
            reviewPicIds.setReviewId(newReview.getReviewId());
            reviewPicIds.setReviewPic(fullPath);
            reviewPic.setReviewPicIds(reviewPicIds);
            reviewPic.setReview(newReview);

            reviewPicRepository.save(reviewPic);
        }
    }


    // 새로운 리뷰 파일 저장 (중복 방지 포함)
    private void saveNewReviewFiles(Review review, List<MultipartFile> files) {
        // 기존 파일 경로 가져오기 (DB에서 조회)
        List<String> existingFilePaths = reviewPicRepository.findFilePathsByReview(review.getReviewId());

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                continue;
            }

            // 파일 유형 확인
            String fileType = file.getContentType();
            if (fileType == null) {
                userMessage.setMessage("유효하지 않은 파일 형식입니다.");
                return;
            }

            String middlePath = String.format("reviews/%s", review.getReviewId());
            String fileCategory = fileType.startsWith("image") ? "images" : "videos";
            String filePath = String.format("%s/%s/", middlePath, fileCategory);
            myFileUtils.makeFolders(filePath);

            // 새로운 파일을 저장할 경로 생성
            String savedFileName = myFileUtils.makeRandomFileName(file);
            String fullPath = filePath + savedFileName;

            // 중복 파일 체크 (이미 DB에 존재하는 경우 저장하지 않음)
            if (existingFilePaths.contains(fullPath)) {
                log.info("이미 존재하는 파일: {}", fullPath);
                continue;
            }

            try {
                myFileUtils.transferTo(file, fullPath);
            } catch (IOException e) {
                userMessage.setMessage("파일 업로드에 실패했습니다.");
                return;
            }

            // ReviewPic 저장
            ReviewPic reviewPic = new ReviewPic();
            ReviewPicIds reviewPicIds = new ReviewPicIds();
            reviewPicIds.setReviewId(review.getReviewId());
            reviewPicIds.setReviewPic(fullPath);
            reviewPic.setReviewPicIds(reviewPicIds);
            reviewPic.setReview(review);

            reviewPicRepository.save(reviewPic);
        }
    }


}
package com.green.acamatch.review;

import com.green.acamatch.acaClass.ClassRepository;
import com.green.acamatch.academy.AcademyRepository;
import com.green.acamatch.academy.model.HB.GeneralReviewDto;
import com.green.acamatch.academy.model.HB.MediaReviewDto;
import com.green.acamatch.accessLog.dailyVisitorStatus.CustomUserDetails;
import com.green.acamatch.config.FilePathConfig;
import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.config.exception.*;
import com.green.acamatch.config.jwt.JwtUser;
import com.green.acamatch.config.security.AuthenticationFacade;
import com.green.acamatch.entity.acaClass.AcaClass;
import com.green.acamatch.entity.joinClass.JoinClass;
import com.green.acamatch.entity.myenum.UserRole;
import com.green.acamatch.entity.review.Review;
import com.green.acamatch.entity.review.ReviewPic;
import com.green.acamatch.entity.review.ReviewPicIds;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.joinClass.JoinClassRepository;

import com.green.acamatch.review.dto.ReviewDto;

import com.green.acamatch.review.model.*;
import com.green.acamatch.user.repository.RelationshipRepository;
import com.green.acamatch.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.web.multipart.MultipartFile;


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
    private final ReviewMapper reviewMapper;
    private final MyFileUtils myFileUtils;
    private final ReviewPicRepository reviewPicRepository;
    private final FilePathConfig filePathConfig;
    private final ReviewMessage reviewMessage;

    @Transactional
    public Integer postReview(List<MultipartFile> pics, ReviewPostReq p) {
        try {
            if (pics == null || pics.isEmpty()) {
                pics = new ArrayList<>();
            }

            if (pics.isEmpty() && p.getComment() == null) {
                throw new CustomException(ReviewErrorCode.REQUIRED_PHOTO_OR_COMMENT);
            }

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
            }
            return 1;
        }catch (CustomException e) {
            e.printStackTrace();
            return 0;
        }
    }

    //학원 전체 리뷰 조회(추가)
    public List<ReviewAcademyAllGetRes> getAcademyReviewsAll(ReviewAcademyAllGetReq req) {
        List<ReviewAcademyAllGetRes> resList = reviewMapper.getAcademyReviewsAll(req);
        if(resList == null || resList.isEmpty()) {
            userMessage.setMessage("작성된 리뷰가 없습니다.");
            return null;
        }
        userMessage.setMessage("작성된 리뷰를 조회하였습니다.");
        return resList;
    }

    //내가 작성한 리뷰 조회(추가)
    public List<ReviewMeGetRes> getMeReviews(ReviewMeGetReq req) {
        List<ReviewMeGetRes> resList = reviewMapper.getMeReviews(req);
        if(resList == null || resList.isEmpty()) {
            userMessage.setMessage("작성한 리뷰가 없습니다.");
            return null;
        }
        userMessage.setMessage("작성한 리뷰를 조회하였습니다.");
        return resList;
    }

    //내가 작성한 리뷰 조회(사진있는거)(추가)
    public List<ReviewMeGetRes> getMeNoPicReviews(ReviewMeGetReq req) {
        List<ReviewMeGetRes> resList = reviewMapper.getMeNoPicReviews(req);


        if(resList == null || resList.isEmpty()) {
            userMessage.setMessage("작성한 리뷰가 없습니다.");
            return null;
        }
        userMessage.setMessage("작성한 리뷰를 조회하였습니다.");
        return resList;
    }

    //내가 작성한 리뷰 조회(사진없는거)(추가)
    public List<ReviewMeGetRes> getMePicReviews(ReviewMeGetReq req) {
        List<ReviewMeGetRes> resList = reviewMapper.getMePicReviews(req);
        if(resList == null || resList.isEmpty()) {
            userMessage.setMessage("작성한 리뷰가 없습니다.");
            return null;
        }
        userMessage.setMessage("작성한 리뷰를 조회하였습니다.");
        return resList;
    }

    //학원관계자가 자신의 학원 리뷰 삭제(추가)
    @Transactional
    public int delAcademyReview(ReviewAcademyDeleteReq req) {
        int result = reviewMapper.delAcademyReview(req);
        userMessage.setMessage("리뷰를 삭제하였습니다.");
        return result;
    }

    //본인이 작성한 리뷰 삭제(추가)
    @Transactional
    public int delMeReview(ReviewMeDeleteReq req) {
        int result = reviewMapper.delMeReview(req);
        userMessage.setMessage("리뷰를 삭제하였습니다.");
        return result;
    }

    //학원 리뷰 조회(사진있는거만)(추가)
    @Transactional
    public List<ReviewAcademyGetRes> getAcademyPicReviews(ReviewAcademyGetReq req) {
        List<ReviewAcademyGetRes> resList = reviewMapper.getAcademyPicReviews(req);
        if(resList == null || resList.isEmpty()) {
            userMessage.setMessage("작성된 리뷰가 없습니다.");
            return resList;
        }
        userMessage.setMessage("리뷰가 조회되었습니다.");
        return resList;
    }

    //학원 리뷰 조회(사진없는거만)(추가)
    @Transactional
    public List<ReviewAcademyGetRes> getAcademyNoPicReviews(ReviewAcademyGetReq req) {
        List<ReviewAcademyGetRes> resList = reviewMapper.getAcademyNoPicReviews(req);
        if(resList == null || resList.isEmpty()) {
            userMessage.setMessage("작성된 리뷰가 없습니다.");
            return resList;
        }
        userMessage.setMessage("리뷰가 조회되었습니다.");
        return resList;
    }

    public GetReviewPicRes getReviewPic(String reviewPic){
        return reviewMapper.getReviewPic(reviewPic);
    }

    @Transactional
    public int updateReview(UpdateReviewReq req, List<MultipartFile> pics) {
        if(pics == null || pics.isEmpty()){
            pics = new ArrayList<>();
        }
        // 1. 리뷰 조회
        Review review = reviewRepository.findById(req.getReviewId()).orElse(null);
        if (review == null) {
            return 0; // 리뷰가 없으면 실패 처리
        }

        // 2. 리뷰 정보 업데이트
        review.setStar(req.getStar());
        if (req.getComment() != null) {
            review.setComment(req.getComment());
        }

        // 3. 기존에 등록된 사진 가져오기
        List<ReviewPic> existingPics = reviewPicRepository.findByReview(review);
        List<String> existingPicNames = existingPics.stream()
                .map(pic -> pic.getReviewPicIds().getReviewPic())
                .collect(Collectors.toList());

        // 4. 새로 등록할 사진 리스트 생성
        List<String> newPicNames = new ArrayList<>();
        List<ReviewPic> newPicList = new ArrayList<>();

        String middlePath = String.format("review/%d", req.getReviewId());


        for (MultipartFile pic : pics) {
            if (pic != null) {
                // 새로운 파일명 생성
                String savedPicName = myFileUtils.makeRandomFileName(pic);
                newPicNames.add(savedPicName);

                // 파일 저장 경로 설정
                String filePath = String.format("%s/%s", middlePath, savedPicName);

                // ReviewPic 엔티티 생성 및 저장
                ReviewPicIds reviewPicIds = new ReviewPicIds();
                reviewPicIds.setReviewId(req.getReviewId());
                reviewPicIds.setReviewPic(savedPicName);

                ReviewPic reviewPic = new ReviewPic();
                reviewPic.setReviewPicIds(reviewPicIds);
                reviewPic.setReview(review);
                newPicList.add(reviewPic);

                // 파일 저장
                try {
                    myFileUtils.transferTo(pic, filePath);
                } catch (IOException e) {
                    String delFolderPath = String.format("%s/%s", myFileUtils.getUploadPath(), middlePath);
                    myFileUtils.deleteFolder(delFolderPath, true);
                }
            }
        }
        reviewPicRepository.saveAll(newPicList);


        // 5. 삭제된 사진 찾기
        List<String> deletedPicNames = existingPicNames.stream()
                .filter(name -> !newPicNames.contains(name))
                .collect(Collectors.toList());

        if (!deletedPicNames.isEmpty()) {
            for (String deletedPic : deletedPicNames) {
                // 5.1 DB에서 삭제
                ReviewPicIds reviewPicIds = new ReviewPicIds();
                reviewPicIds.setReviewId(req.getReviewId());
                reviewPicIds.setReviewPic(deletedPic);
                reviewPicRepository.deleteById(reviewPicIds);

                // 5.2 파일 시스템에서 삭제
                String filePathToDelete = String.format("%s/%s/%s", myFileUtils.getUploadPath(), middlePath, deletedPic);
                myFileUtils.deleteFile(filePathToDelete);
            }
        }

        // 6. 리뷰 저장 및 성공 메시지 반환

        reviewMessage.setMessage("리뷰 정보 수정이 완료되었습니다.");
        reviewRepository.save(review);

        return 1;
    }
}
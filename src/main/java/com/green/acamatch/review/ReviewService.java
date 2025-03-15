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
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewPicRepository reviewPicRepository;
    private final MyFileUtils myFileUtils;

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

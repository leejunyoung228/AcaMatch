package com.green.acamatch.review;

import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.review.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.transform.Result;
import java.util.List;

@Tag(name = "리뷰 관리", description = "리뷰 등록, 가져오기, 수정, 삭제")
@RequiredArgsConstructor
@RestController
@RequestMapping("review")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserMessage userMessage;

    @PostMapping
    @Operation(summary = "리뷰 등록", description = "postMan으로 테스트")
    public ResultResponse<Integer> postReview(@RequestPart List<MultipartFile> pics, @RequestPart ReviewPostReq p) {
        int result = reviewService.postReview(pics, p);
        return ResultResponse.<Integer>builder()
                .resultMessage(result == 1 ? "리뷰 등록 성공" : "리뷰 등록 실패")
                .resultData(result)
                .build();
    }

    @GetMapping("academy/all")
    @Operation(summary = "학원 리뷰 전체 조회(새로생성)")
    public ResultResponse<List<ReviewAcademyAllGetRes>> getAcademyReviewsAll(@ParameterObject @ModelAttribute ReviewAcademyAllGetReq req) {
        List<ReviewAcademyAllGetRes> resList = reviewService.getAcademyReviewsAll(req);
        return ResultResponse.<List<ReviewAcademyAllGetRes>>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(resList)
                .build();
    }

    @GetMapping("me")
    @Operation(summary = "본인이작성한 리뷰조회(새로생성)")
    public ResultResponse<List<ReviewMeGetRes>> getMeReviews(@ParameterObject @ModelAttribute ReviewMeGetReq req) {
        List<ReviewMeGetRes> resList = reviewService.getMeReviews(req);
        return ResultResponse.<List<ReviewMeGetRes>>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(resList)
                .build();
    }

    @GetMapping("me/pic")
    @Operation(summary = "본인이작성한 리뷰조회(사진있는거만)(새로생성)")
    public ResultResponse<List<ReviewMeGetRes>> getMeNoPicReviews(@ParameterObject @ModelAttribute ReviewMeGetReq req) {
        List<ReviewMeGetRes> resList = reviewService.getMeNoPicReviews(req);
        return ResultResponse.<List<ReviewMeGetRes>>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(resList)
                .build();
    }

    @GetMapping("me/noPic")
    @Operation(summary = "본인이작성한 리뷰조회(사진없는거만)(새로생성)")
    public ResultResponse<List<ReviewMeGetRes>> getMePicReviews(@ParameterObject @ModelAttribute ReviewMeGetReq req) {
        List<ReviewMeGetRes> resList = reviewService.getMePicReviews(req);
        return ResultResponse.<List<ReviewMeGetRes>>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(resList)
                .build();
    }

    @DeleteMapping("academy")
    @Operation(summary = "학원관계자가 본인 학원 리뷰 삭제 (새로생성)")
    public ResultResponse<Integer> delAcademyReview(@ParameterObject @ModelAttribute ReviewAcademyDeleteReq req) {
        int result = reviewService.delAcademyReview(req);
        return ResultResponse.<Integer>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(result)
                .build();
    }

    @DeleteMapping("me")
    @Operation(summary = "본인이 작성한 리뷰 삭제(새로생성)")
    public ResultResponse<Integer> delMeReview(@ParameterObject @ModelAttribute ReviewMeDeleteReq req) {
        int result = reviewService.delMeReview(req);
        return ResultResponse.<Integer>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(result)
                .build();
    }






}
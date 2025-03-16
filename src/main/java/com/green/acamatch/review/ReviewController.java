package com.green.acamatch.review;

import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.review.model.ReviewPostReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "리뷰 관리", description = "리뷰 등록, 가져오기, 수정, 삭제")
@RequiredArgsConstructor
@RestController
@RequestMapping("review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary = "리뷰 등록", description = "postMan으로 테스트")
    public ResultResponse<Integer> postReview(@RequestPart List<MultipartFile> pics, @RequestPart ReviewPostReq p) {
        int result = reviewService.postReview(pics, p);
        return ResultResponse.<Integer>builder()
                .resultMessage(result == 1 ? "리뷰 등록 성공" : "리뷰 등록 실패")
                .resultData(result)
                .build();
    }
}
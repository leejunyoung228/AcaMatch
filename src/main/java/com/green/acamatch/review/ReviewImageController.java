package com.green.acamatch.review;

import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.review.model.ReviewPostReqForParent;
import com.green.acamatch.review.model.ReviewUpdateReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("image-review")
@Tag(name = "학원 사진 리뷰(후기) 관리")
public class ReviewImageController {

    private final ReviewImageService service;
    private final UserMessage userMessage;

    @PostMapping("/create")
    @Operation(summary = "사진 후기 등록", description = "학생 또는 학부모가 리뷰를 등록합니다.")
    public ResultResponse<Integer> createReview(
            @RequestPart("review") ReviewPostReqForParent req,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        int result;
        try {
            result = service.createReview(req, files);
        } catch (CustomException e) {
            userMessage.setMessage(e.getMessage()); // 예외 메시지를 userMessage에 설정

            return ResultResponse.<Integer>builder()
                    .resultMessage(userMessage.getMessage())  // 예외 메시지 반환
                    .resultData(0)  // 실패
                    .build();
        }

        return ResultResponse.<Integer>builder()
                .resultMessage(userMessage.getMessage()) // 정상 처리 메시지 반환
                .resultData(result)
                .build();
    }

    @PostMapping("/update")
    @Operation(summary = "사진 포함 리뷰 수정", description = "리뷰 수정 시 기존 사진 삭제 및 새로운 사진 추가 가능")
    public ResultResponse<Integer> updateReviewWithFiles(
            @RequestPart("review") ReviewUpdateReq req,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestParam(value = "deletedFiles", required = false) List<String> deletedFiles) {

        int result;
        try {
            result = service.updateReviewWithFiles(req, files, deletedFiles);
        } catch (CustomException e) {
            userMessage.setMessage(e.getMessage());

            return ResultResponse.<Integer>builder()
                    .resultMessage(userMessage.getMessage())
                    .resultData(0)
                    .build();
        }

        return ResultResponse.<Integer>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(result)
                .build();
    }



}

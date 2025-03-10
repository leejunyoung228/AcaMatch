package com.green.acamatch.review;

import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.review.dto.MyReviewDto;
import com.green.acamatch.review.dto.ReviewDto;
import com.green.acamatch.review.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/review")
@Tag(name = "학원 리뷰 관리")
public class ReviewController {
    private final ReviewService service;
    private final UserMessage userMessage;


    @PostMapping("/user")
    @Operation(summary = "리뷰 등록", description = "학생 유저가 리뷰를 등록합니다.")
    public ResultResponse<Integer> createReview(@RequestBody ReviewPostReqForParent req) {
        service.createReview(req);
        return ResultResponse.<Integer>builder()
                .resultMessage(userMessage.getMessage()) // 서비스에서 설정된 메시지 사용
                .resultData(1)
                .build();
    }

//    // 학생 유저 리뷰 등록
//    @PostMapping("/user")
//    @Operation(summary = "리뷰 등록", description = "학생 유저가 리뷰를 등록합니다.")
//    public ResultResponse<Integer> addReview(@RequestBody ReviewPostReq req) {
//        int result = service.addReview(req); // 0 또는 1 반환
//        return ResultResponse.<Integer>builder()
//                .resultMessage(userMessage.getMessage()) // 서비스에서 설정된 메시지 사용
//                .resultData(result)
//                .build();
//    }

    // 학생 유저 리뷰 수정
    @PutMapping("/user")
    @Operation(summary = "리뷰 수정", description = "학생 유저가 자신의 리뷰를 수정합니다.")
    public ResultResponse<Integer> updateReview(@RequestBody ReviewUpdateReq req) {
        int result = service.updateReview(req); // 0 또는 1 반환
        return ResultResponse.<Integer>builder()
                .resultMessage(userMessage.getMessage()) // 서비스에서 설정된 메시지 사용
                .resultData(result)
                .build();
    }



    // 학원 관계자 리뷰 삭제
    @DeleteMapping("/academy")
    @Operation(
            summary = "학원 관계자 리뷰 삭제",
            description = "학원 관계자가 본인의 학원 리뷰를 삭제합니다. 사이트 관리자도 이 API로 특정 리뷰를 삭제 할 수 있습니다." +
                    "여기서 입력하는 userId는 학원 관계자 본인의 유저 아이디여야 합니다. (학원 관계자 검증)"
    )
    public ResultResponse<Integer> deleteReviewByAcademy(@RequestBody ReviewDelMyacademyReq req) {
        int result = service.deleteReviewByAcademyAndAdmin(req); // 0 또는 1 반환
        return ResultResponse.<Integer>builder()
                .resultMessage(userMessage.getMessage()) // 서비스에서 설정된 메시지 사용
                .resultData(result)
                .build();
    }

    // 본인 작성 리뷰 삭제
    @DeleteMapping("/user")
    @Operation(
            summary = "본인 작성 리뷰 삭제",
            description = "사용자가 본인이 작성한 리뷰를 삭제합니다."
    )
    public ResultResponse<Integer> deleteReviewByUser(@RequestBody ReviewDelReq req) {
        int result = service.deleteReviewByUser(req); // 0 또는 1 반환
        return ResultResponse.<Integer>builder()
                .resultMessage(userMessage.getMessage()) // 서비스에서 설정된 메시지 사용
                .resultData(result)
                .build();
    }


    // 본인 학원 리뷰 조회
    @GetMapping("/my-academy")
    @Operation(
            summary = "학원 관계자의 본인 학원 리뷰 조회",
            description = "학원 관계자가 본인의 학원에 대해 작성된 리뷰를 조회합니다."
    )
    public ResultResponse<List<ReviewDto>> getMyAcademyReviews(
            @RequestParam long userId,
            @RequestParam long acaId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        MyAcademyReviewListGetReq req = new MyAcademyReviewListGetReq(page, size); // 페이징 정보를 포함한 요청 객체 생성
        req.setUserId(userId); // 유저 ID 설정
        req.setAcaId(acaId);
        List<ReviewDto> reviews = service.getMyAcademyReviews(req); // 서비스 호출
        return ResultResponse.<List<ReviewDto>>builder()
                .resultMessage(userMessage.getMessage()) // 사용자 메시지 반환
                .resultData(reviews) // 리뷰 데이터 반환
                .build();
    }


    // 학원 상세페이지에서 리뷰 조회
    @GetMapping("/academy")
    @Operation(summary = "학원 리뷰 조회", description = "특정 학원의 상세페이지 리뷰를 조회합니다.")
    public ResultResponse<List<ReviewDto>> getAcademyReviewsForPublic(
            @RequestParam long acaId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        ReviewListGetReq req = new ReviewListGetReq(page, size);
        req.setAcaId(acaId); // acaId 설정
        List<ReviewDto> reviews = service.getAcademyReviewsForPublic(req);
        return ResultResponse.<List<ReviewDto>>builder()
                .resultMessage("리뷰 조회 성공")
                .resultData(reviews)
                .build();
    }

    // 본인이 작성한 리뷰 목록 조회
    @GetMapping("/user")
    @Operation(
            summary = "본인이 작성한 리뷰 목록 조회",
            description = "특정 사용자가 본인이 작성한 리뷰 목록을 불러옵니다."
    )
    public ResultResponse<List<MyReviewDto>> getReviewsByUser(
            @RequestParam long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        MyReviewGetReq req = new MyReviewGetReq(page, size); // 페이징 정보를 포함한 요청 객체 생성
        req.setUserId(userId); // 유저 ID 설정
        List<MyReviewDto> reviews = service.getReviewsByUserId(req); // 서비스 호출
        return ResultResponse.<List<MyReviewDto>>builder()
                .resultMessage(userMessage.getMessage()) // 사용자 메시지 반환
                .resultData(reviews) // 리뷰 데이터 반환
                .build();
    }

}
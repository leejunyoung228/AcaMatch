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


//    // 학원 상세페이지에서 리뷰 조회
//    @GetMapping("/public-academy")
//    @Operation(summary = "학원 리뷰 조회", description = "특정 학원의 상세페이지 리뷰를 조회합니다.")
//    public ResultResponse<List<ReviewDto>> getAcademyReviewsForPublic(
//            @RequestParam long acaId,
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "20") int size) {
//        ReviewListGetReq req = new ReviewListGetReq(page, size);
//        req.setAcaId(acaId); // acaId 설정
//        List<ReviewDto> reviews = service.getAcademyReviewsForPublic(req);
//        return ResultResponse.<List<ReviewDto>>builder()
//                .resultMessage("리뷰 조회 성공")
//                .resultData(reviews)
//                .build();
//    }

    // 본인이 작성한 리뷰 목록 조회
    @GetMapping("/user/my-media")
    @Operation(summary = "본인이 작성한 미디어 리뷰 조회", description = "로그인한 사용자가 작성한 미디어 리뷰 목록을 가져옵니다.mediaStartIdx 기본 값은 0")
    public ResultResponse<MediaReviewResponseDto> getUserMediaReviews(
            @ParameterObject @ModelAttribute MyMediaReviewGetReq req) {


        MediaReviewResponseDto response = service.getUserMediaReviews(req);

        return ResultResponse.<MediaReviewResponseDto>builder()
                .resultMessage("사용자의 미디어 리뷰 목록 조회 성공")
                .resultData(response)
                .build();
    }

    @GetMapping("/user/my-general")
    @Operation(summary = "본인이 작성한 일반 리뷰 조회", description = "로그인한 사용자가 작성한 일반 리뷰 목록을 가져옵니다. generalStartIdx 기본 값은 0")
    public ResultResponse<GeneralReviewResponseDto> getUserGeneralReviews(
            @ParameterObject @ModelAttribute MyGeneralReviewGetReq req) {

        GeneralReviewResponseDto response = service.getUserGeneralReviews(req);

        return ResultResponse.<GeneralReviewResponseDto>builder()
                .resultMessage("사용자의 일반 리뷰 목록 조회 성공")
                .resultData(response)
                .build();
    }
}
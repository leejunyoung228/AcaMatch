package com.green.acamatch.academyCost;

import com.green.acamatch.academyCost.model.KakaoApproveResponse;
import com.green.acamatch.academyCost.model.KakaoPayPostReq;
import com.green.acamatch.academyCost.model.KakaoReadyResponse;
import com.green.acamatch.config.exception.AcademyCostException;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.entity.academyCost.AcademyCost;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Tag(name = "카카오페이")
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    /**
     * 결제요청
     */
    @PostMapping("/ready")
    public KakaoReadyResponse readyToKakaoPay(@ParameterObject KakaoPayPostReq req) {

        return kakaoPayService.kakaoPayReady(req);
    }

    /**
     * 결제성공
     */
    @PostMapping ("/success")
    public ResponseEntity<KakaoApproveResponse> afterPayRequest(@RequestParam("pg_token") String pgToken, int orderId) {

        KakaoApproveResponse kakaoApprove = kakaoPayService.approveResponse(pgToken, orderId);

        return new ResponseEntity<>(kakaoApprove, HttpStatus.OK);
    }

    /**
     * 결제 진행 중 취소
     */
    @GetMapping("/cancel")
    public void cancel() {

        throw new CustomException(AcademyCostException.PAY_CANCEL);
    }

    /**
     * 결제실패
     */

    @GetMapping("/fail")
    public void fail() {

        throw new CustomException(AcademyCostException.PAY_FAILED);
    }

    @GetMapping("/recent")
    public ResultResponse<List<AcademyCost>> getRecentPayments() {
        return ResultResponse.<List<AcademyCost>>builder()
                .resultData(kakaoPayService.getRecentPayments())
                .resultMessage("사이트 관리자가 결제 내역 불러오기 성공")
                .build();
    }

    @GetMapping("/recent/{userId}")
    public ResultResponse<List<AcademyCost>> getRecentPaymentsByUser(@PathVariable Long userId) {
        return ResultResponse.<List<AcademyCost>>builder()
                .resultData(kakaoPayService.getRecentPaymentsByUserId(userId))
                .resultMessage("userId에 맞는 결제 내역 불러오기 성공")
                .build();
    }
}
package com.green.acamatch.academyCost;

import com.green.acamatch.academyCost.model.KakaoApproveResponse;
import com.green.acamatch.academyCost.model.KakaoPayPostReq;
import com.green.acamatch.academyCost.model.KakaoReadyResponse;
import com.green.acamatch.config.exception.AcademyCostException;
import com.green.acamatch.config.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
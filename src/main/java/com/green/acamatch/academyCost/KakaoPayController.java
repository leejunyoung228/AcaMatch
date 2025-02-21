package com.green.acamatch.academyCost;

import com.green.acamatch.academyCost.model.KakaoApproveResponse;
import com.green.acamatch.academyCost.model.KakaoPayPostReq;
import com.green.acamatch.academyCost.model.KakaoReadyResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
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
    public ResponseEntity<KakaoApproveResponse> afterPayRequest(@RequestParam("pg_token") String pgToken, String tid) {

        KakaoApproveResponse kakaoApprove = kakaoPayService.approveResponse(pgToken);

        return new ResponseEntity<>(kakaoApprove, HttpStatus.OK);
    }

    /**
     * 결제 진행 중 취소
     */
    @GetMapping("/cancel")
    public void cancel() {

        //throw new BusinessLogicException(ExceptionCode.PAY_CANCEL);
    }

    /**
     * 결제실패
     */

    @GetMapping("/fail")
    public void fail() {

        //throw new BusinessLogicException(ExceptionCode.PAY_FAILED);
    }
}
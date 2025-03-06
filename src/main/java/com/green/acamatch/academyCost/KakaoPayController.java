package com.green.acamatch.academyCost;

import com.green.acamatch.academy.Service.PremiumService;
import com.green.acamatch.academyCost.model.KakaoApproveResponse;
import com.green.acamatch.academyCost.model.KakaoPayPostReq;
import com.green.acamatch.academyCost.model.KakaoReadyResponse;
import com.green.acamatch.config.exception.AcademyCostException;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.entity.academyCost.AcademyCost;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
    private final AcademyCostMessage academyCostMessage;

    /**
     * 결제요청
     */
    @PostMapping("/ready")
    @Operation(summary = "결제 준비", description = "aca_id는 프리미엄 학원 결제 시에만 필요한 값입니다. 교재 결제할때는 aca_id 없이 보내시면 되고" +
            "교재 여러개 결제한다면 예시로 나와 있는 products를 교재 종류 만큼 쓰시면 됩니다." +
            "성공적으로 excute가 된다면 tid랑 이것저것 나올텐데, 그 중 pc_url을 사용하시면 되는데" +
            "그 pc_url를 검색창에 그대로 복사해서 붙여넣어보시면 바로 결제 페이지가 나옵니다." +
            "그 상태에서 결제하시면 페이지가 자동으로 넘어가게 되는데, url에 pg_token이라는게 붙어서 넘어옵니다." +
            "그러면 그 pg_token이랑 결제 준비에서 받았던 tid를 kakaoPay/success에 입력하시면 결제가 완료됩니다.")
    public ResultResponse<KakaoReadyResponse> readyToKakaoPay(@RequestBody KakaoPayPostReq req, HttpServletRequest request) {
        return ResultResponse.<KakaoReadyResponse>builder()
                .resultData(kakaoPayService.kakaoPayReady(request, req))
                .resultMessage(academyCostMessage.getMessage())
                .build();
    }

    /**
     * 결제성공
     */
    @PostMapping ("/success")
    public ResponseEntity<KakaoApproveResponse> afterPayRequest(@RequestParam("pg_token") String pgToken,
                                                                @RequestParam("TId") String TId) {

        KakaoApproveResponse kakaoApprove = kakaoPayService.approveResponse(pgToken, TId);

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
package com.green.acamatch.refund;

import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.refund.model.PostRefundReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/refund")
@RestController
@RequiredArgsConstructor
@Tag(name = "환불")
public class RefundController {
    private final RefundService refundService;

    @PostMapping("postRefund")
    @Operation(summary = "환불 신청")
    public ResultResponse<Integer> postRefund(@ParameterObject PostRefundReq req){
        int result = refundService.postRefund(req);
        return ResultResponse.<Integer>builder()
                .resultMessage("환불 등록 성공")
                .resultData(result)
                .build();
    }
}

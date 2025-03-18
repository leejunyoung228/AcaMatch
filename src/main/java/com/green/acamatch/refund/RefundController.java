package com.green.acamatch.refund;

import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.refund.model.GetRefundListByAcaUserIdRes;
import com.green.acamatch.refund.model.GetRefundRes;
import com.green.acamatch.refund.model.PostRefundReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("getRefundList")
    @Operation(summary = "환불 내역 가져오기")
    public ResultResponse<List<GetRefundRes>> getRefundList(){
        List<GetRefundRes> res = refundService.getRefundRes();
        return ResultResponse.<List<GetRefundRes>>builder()
                .resultMessage("출력 성공")
                .resultData(res)
                .build();
    }

    @GetMapping("getRefundListByUserId/{userId}")
    @Operation(summary = "유저 PK를 받아 환불 내역 가져오기")
    public ResultResponse<List<GetRefundRes>> getRefundListByUserId(@PathVariable long userId){
        List<GetRefundRes> res = refundService.getRefundResList(userId);
        return ResultResponse.<List<GetRefundRes>>builder()
                .resultMessage("출력 성공")
                .resultData(res)
                .build();
    }

    @PutMapping("updateRefund")
    @Operation(summary = "환불 상태를 환불 전 -> 환불 완료로 교체")
    public ResultResponse<Integer> updateRefund(@ParameterObject long refundId){
        refundService.updateRefund(refundId);
        return ResultResponse.<Integer>builder()
                .resultMessage("수정 완료")
                .resultData(1)
                .build();
    }

    @GetMapping("getRefundListByAcaUserId/{userId}")
    @Operation(summary = "학원 관계자가 환불 내역 보기", description = "userId 2번에 데이터 있어요")
    public ResultResponse<List<GetRefundListByAcaUserIdRes>> getRefundListByAcaUserId(@PathVariable long userId){
        List<GetRefundListByAcaUserIdRes> res = refundService.getRefundListByAcaUserId(userId);
        return ResultResponse.<List<GetRefundListByAcaUserIdRes>>builder()
                .resultMessage("조회 완료")
                .resultData(res)
                .build();
    }
}

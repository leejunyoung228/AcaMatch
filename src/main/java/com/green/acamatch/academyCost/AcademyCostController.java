package com.green.acamatch.academyCost;

import com.green.acamatch.academyCost.model.GetAcademyCostInfoByCostId;
import com.green.acamatch.academyCost.model.GetAcademyCostInfoByMonth;
import com.green.acamatch.academyCost.model.GetSettlementListReq;
import com.green.acamatch.academyCost.model.GetSettlementListRes;
import com.green.acamatch.config.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/academyCost")
@Tag(name = "결제")
public class AcademyCostController {
    private final AcademyCostService academyCostService;

    @GetMapping("getAcademyCostInfoByMonth")
    @Operation(summary = "사이트 관리자가 월 수익 보기")
    public ResultResponse<GetAcademyCostInfoByMonth> getAcademyCostInfoByMonth() {
        return ResultResponse.<GetAcademyCostInfoByMonth>builder()
                .resultMessage("출력 성공")
                .resultData(academyCostService.getAcademyCostInfoByMonth())
                .build();
    }

    @GetMapping("getSettlementList")
    @Operation(summary = "정산 조회", description = "page, size만 넣고 실행해보면 맨 위에 이상한 놈 하나 뜨는데 status, year, month 중 하나라도 넣고 실행하면 안나옵니다." +
            "그래서 맨 처음 이걸 실행하는 페이지에 들어가면 현재 년도와 월에 대한 정보를 주시면 좋아요!")
    public ResultResponse<List<GetSettlementListRes>> getSettlementList(@ParameterObject GetSettlementListReq req) {
        return ResultResponse.<List<GetSettlementListRes>>builder()
                .resultMessage("출력 성공")
                .resultData(academyCostService.getSettlementList(req))
                .build();
    }

    @GetMapping("getAcademyCostInfoByCostId/{costId}")
    @Operation(summary = "주문 상세 내역")
    public ResultResponse<GetAcademyCostInfoByCostId> getAcademyCostInfoByCostId(@PathVariable long costId) {
        return ResultResponse.<GetAcademyCostInfoByCostId>builder()
                .resultMessage("조회 성공")
                .resultData(academyCostService.getAcademyCostInfoByCostId(costId))
                .build();
    }
}

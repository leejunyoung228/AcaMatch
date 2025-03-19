package com.green.acamatch.academyCost;

import com.green.acamatch.academyCost.model.*;
import com.green.acamatch.config.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/academyCost")
@Tag(name = "결제")
public class AcademyCostController {
    private final AcademyCostService academyCostService;
    private final AcademyCostMessage academyCostMessage;

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
    @Operation(summary = "주문 상세 내역", description = "orderType 0: 학원, 1: 책, 2:프리미엄 학원")
    public ResultResponse<GetAcademyCostInfoByCostId> getAcademyCostInfoByCostId(@PathVariable long costId) {
        return ResultResponse.<GetAcademyCostInfoByCostId>builder()
                .resultMessage("조회 성공")
                .resultData(academyCostService.getAcademyCostInfoByCostId(costId))
                .build();
    }

    @PutMapping("updateStatus/{costIds}")
    @Operation(summary = "정산 X -> 정산 O로 처리", description = "어떻게 만드실지 몰라서 일단 결제 PK를 받아와서 정산 상태인 status를 1로 변경하도록 만들었습니다.")
    public ResultResponse<Integer> updateStatus(@PathVariable String costIds) {
        int result = academyCostService.updateStatuses(costIds);
        return ResultResponse.<Integer>builder()
                .resultMessage(academyCostMessage.getMessage())
                .resultData(result)
                .build();
    }

    @GetMapping("getProductInfo")
    @Operation(summary = "모든 상품 정보 조회하는 api")
    public ResultResponse<List<GetProductInfoRes>> getProductInfo() {
        List<GetProductInfoRes> res = academyCostService.getProductInfo();
        return ResultResponse.<List<GetProductInfoRes>>builder()
                .resultMessage("조회 성공")
                .resultData(res)
                .build();
    }

    @GetMapping("getAcademyCostListByUser/{userId}")
    @Operation(summary = "유저 입장에서 결제 내역보기", description = "acaId랑 acaName, classOrBookName이 없는 애가 있는데 이건" +
            "프리미엄 학원 결제 내역이라서 안나오는 겁니다.")
    public ResultResponse<List<GetAcademyCostListByUserRes>> getAcademyCostListByUser(@PathVariable long userId) {
        List<GetAcademyCostListByUserRes> result = academyCostService.getAcademyCostListByUser(userId);
        return ResultResponse.<List<GetAcademyCostListByUserRes>>builder()
                .resultMessage(academyCostMessage.getMessage())
                .resultData(result)
                .build();
    }
}

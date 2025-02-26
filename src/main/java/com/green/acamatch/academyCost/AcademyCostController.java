package com.green.acamatch.academyCost;

import com.green.acamatch.academyCost.model.GetAcademyCostInfoByMonth;
import com.green.acamatch.config.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/academyCost")
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
}

package com.green.acamatch.academy.Controller;

import com.green.acamatch.academy.Service.PremiumService;
import com.green.acamatch.academy.model.JW.AcademyMessage;
import com.green.acamatch.academy.premium.model.*;
import com.green.acamatch.config.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("academy/premium")
@Tag(name = "프리미엄")
public class PremiumController {
    private final PremiumService premiumService;
    private final AcademyMessage academyMessage;

    /*//프리미엄학원신청
    @PostMapping
    @Operation(summary = "프리미엄 신청")
    public ResultResponse<Integer> postPremiumAcademy(@RequestBody PremiumPostReq req) {
        int result = premiumService.insPremium(req);
        return ResultResponse.<Integer>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(result)
                .build();
    }*/

    //프리미엄승인
    @PutMapping
    @Operation(summary = "프리미엄 승인", description = "프리미엄 승인은 preCheck = 1 보내주세요.")
    public ResultResponse<Integer> putPremiumCheck(@RequestBody PremiumUpdateReq req) {
        int result = premiumService.updPremium(req);
        return ResultResponse.<Integer>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(result)
                .build();
    }

    //프리미엄 학원 조회
    @GetMapping
    @Operation(summary = "프리미엄학원 조회")
    public ResultResponse<List<PremiumGetRes>> getPremiumAcademy(@RequestParam("page") int page,
                                                                 @RequestParam("size") int size) {

        Pageable pageable = PageRequest.of(page-1, size);
        // Pageable에서 sort 제외하고 데이터를 처리

        List<PremiumGetRes> resList = premiumService.getPremium(pageable);
        return ResultResponse.<List<PremiumGetRes>>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(resList)
                .build();
    }

    //프리미엄,배너타입학원조회
    @GetMapping("bannerType")
    @Operation(summary = "프리미엄학원, 배너타입 조회")
    public ResultResponse<List<PremiumBannerGetRes>> getPremiumBannerTypeAcademy(@RequestParam("page") int page,
                                                                                 @RequestParam("size") int size) {

        Pageable pageable = PageRequest.of(page-1, size);

        List<PremiumBannerGetRes> resList = premiumService.getPremiumBannerType(pageable);
        return ResultResponse.<List<PremiumBannerGetRes>>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(resList)
                .build();
    }

    //프리미엄학원삭제
    @DeleteMapping
    @Operation(summary = "프리미엄학원 삭제")
    public ResultResponse<Integer> deletePremiumAcademy(@RequestBody PremiumDeleteReq req) {
        int result = premiumService.delPremium(req);
        return ResultResponse.<Integer>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(result)
                .build();
    }
}

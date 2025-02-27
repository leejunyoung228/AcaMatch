package com.green.acamatch.academy.Controller;

import com.green.acamatch.academy.Service.BannerService;
import com.green.acamatch.academy.banner.model.BannerPicShowUpdateReq;
import com.green.acamatch.academy.banner.model.BannerTypeUpdateReq;
import com.green.acamatch.academy.banner.model.BannerPostReq;
import com.green.acamatch.academy.model.JW.AcademyMessage;
import com.green.acamatch.config.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("banner")
@Tag(name = "배너")
public class BannerController {
    private final BannerService bannerService;
    private final AcademyMessage academyMessage;

    @PostMapping
    @Operation(summary = "배너신청")
    public ResultResponse<Integer> postBanner(@RequestPart MultipartFile topBannerPic, @RequestPart MultipartFile bottomBannerPic
                        , @RequestPart MultipartFile leftBannerPic, @RequestPart MultipartFile rightBannerPic
                        , @RequestPart BannerPostReq req) {
        bannerService.postBanner(topBannerPic, bottomBannerPic, leftBannerPic, rightBannerPic, req);
        return ResultResponse.<Integer>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(1)
                .build();
    }

    @PutMapping("agree")
    @Operation(summary = "배너승인")
    public ResultResponse<Integer> putBannerType(@RequestBody BannerTypeUpdateReq req) {
        bannerService.updateBannerType(req.getAcaId(), req.getBannerType());
        return ResultResponse.<Integer>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(1)
                .build();
    }

    @PutMapping
    @Operation(summary = "배너 각각 활성화/비활성화", description = "position은 상: 1, 하: 2, 좌: 3, 우:4, show는 활성화: 1, 비활성화: 2")
    public ResultResponse<Integer> putBannerShow(@RequestBody BannerPicShowUpdateReq req) {
        bannerService.updateBannerShow(req.getAcaId(), req.getBannerPosition(), req.getBannerShow());
        return ResultResponse.<Integer>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(1)
                .build();
    }
}

package com.green.acamatch.academy.Controller;

import com.green.acamatch.academy.Service.BannerService;
import com.green.acamatch.academy.banner.model.*;
import com.green.acamatch.academy.model.JW.AcademyMessage;
import com.green.acamatch.config.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kotlinx.serialization.Required;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.transform.Result;
import java.util.List;

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
    public ResultResponse<Integer> postBanner(@RequestPart(required = false) MultipartFile topBannerPic, @RequestPart(required = false) MultipartFile bottomBannerPic
                                            , @RequestPart(required = false) MultipartFile rightBannerPic
                                            , @RequestPart BannerPostReq req) {
        bannerService.postBanner(topBannerPic, bottomBannerPic, rightBannerPic, req);
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
    @Operation(summary = "배너 각각 활성화/비활성화", description = "position은 상: 1, 하: 2, 좌: 3, 우:4, show는 활성화: 1, 비활성화: 0")
    public ResultResponse<Integer> putBannerShow(@RequestBody BannerPicShowUpdateReq req) {
        bannerService.updateBannerShow(req.getAcaId(), req.getBannerPosition(), req.getBannerShow());
        return ResultResponse.<Integer>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(1)
                .build();
    }

    @GetMapping("position")
    @Operation(summary = "포지션 별 배너 조회", description = "position 1: TOP, 2: BOTTOM, 3: LEFT, 4: RIGHT")
    public ResultResponse<List<BannerByPositionGetRes>> getBannerByPosition(BannerByPositionGetReq req) {

        List<BannerByPositionGetRes> res = bannerService.getBannerByPosition(req.getAcaId(), req.getBannerPosition());
        return ResultResponse.<List<BannerByPositionGetRes>>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(res)
                .build();
    }

    @GetMapping
    @Operation(summary = "프리미엄학원의 배너 조회")
    public ResultResponse<List<BannerGetRes>> getBanner(BannerGetReq req) {

        List<BannerGetRes> res = bannerService.getBanner(req.getAcaId());
        return ResultResponse.<List<BannerGetRes>>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(res)
                .build();
    }

    @GetMapping("all")
    @Operation(summary = "모든 프리미엄학원 배너 조회 ")
    public ResultResponse<List<BannerGetRes>> getBannerAll() {

        List<BannerGetRes> res = bannerService.getBannerAll();
        return ResultResponse.<List<BannerGetRes>>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(res)
                .build();
    }

    @PutMapping("pic")
    @Operation(summary = "배너 사진 수정")
    public ResultResponse<Integer> updBanner(@RequestPart MultipartFile pic, @RequestPart BannerUpdateReq req) {
        bannerService.updBannerPic(pic, req);
        return ResultResponse.<Integer>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(1)
                .build();
    }

    @DeleteMapping
    @Operation(summary = "배너 삭제")
    public ResultResponse<Integer> delBanner(BannerDeleteReq req) {
        bannerService.delBanner(req.getAcaId(), req.getBannerPosition());
        return ResultResponse.<Integer>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(1)
                .build();
    }
}

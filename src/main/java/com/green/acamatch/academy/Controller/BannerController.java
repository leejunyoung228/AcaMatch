package com.green.acamatch.academy.Controller;

import com.green.acamatch.academy.Service.BannerService;
import com.green.acamatch.academy.banner.model.BannerPostReq;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("banner")
public class BannerController {
    private final BannerService bannerService;

    @PostMapping
    @Operation(summary = "배너신청")
    public int postBanner(@RequestPart MultipartFile topBannerPics, @RequestPart MultipartFile bottomBannerPics
                        , @RequestPart MultipartFile leftBannerPics, @RequestPart MultipartFile rightBannerPics
                        , @RequestPart BannerPostReq req) {
        bannerService.postBanner(topBannerPics, bottomBannerPics, leftBannerPics, rightBannerPics, req);
        return 1;
    }
}

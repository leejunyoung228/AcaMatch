package com.green.acamatch.academy.Controller;

import com.green.acamatch.academy.Service.PremiumService;
import com.green.acamatch.academy.premium.model.PremiumDeleteReq;
import com.green.acamatch.academy.premium.model.PremiumPostReq;
import com.green.acamatch.config.model.ResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("premium")
public class PremiumController {
    private PremiumService premiumService;

    @PostMapping
    public ResultResponse<Integer> postPremiumAcademy(@RequestBody PremiumPostReq req) {
        int result = premiumService.insPremium(req);
        return null;
    }

    @DeleteMapping
    public ResultResponse<Integer> deletePremiumAcademy(@RequestBody PremiumDeleteReq req) {
        int result = premiumService.delPremium(req);
        return null;
    }
}

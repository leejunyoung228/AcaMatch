package com.green.acamatch.academy.Controller;

import com.green.acamatch.config.model.ResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("premium")
public class PremiumController {

    @PostMapping
    public ResultResponse<Integer> postPremiumAcademy() {
        return null;
    }

    @DeleteMapping
    public ResultResponse<Integer> deletePremiumAcademy() {
        return null;
    }
}

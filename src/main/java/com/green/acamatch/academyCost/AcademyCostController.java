package com.green.acamatch.academyCost;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/academyCost")
public class AcademyCostController {
    private final AcademyCostService academyCostService;
}

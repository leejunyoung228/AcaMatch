package com.green.acamatch.academy.Service;

import com.green.acamatch.academy.PremiumRepository;
import com.green.acamatch.academy.model.JW.AcademyMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PremiumService {
    private final AcademyMessage academyMessage;
    private final PremiumRepository premiumRepository;

    //프리미엄학원 신청
    public int insPremium() {
        return 0;
    }
}

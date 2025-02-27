package com.green.acamatch.academyCost;

import com.green.acamatch.academyCost.model.GetAcademyCostInfoByMonth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AcademyCostService {
    private final AcademyCostMapper academyCostMapper;

    public GetAcademyCostInfoByMonth getAcademyCostInfoByMonth() {
        return academyCostMapper.getAcademyCostInfo();
    }
}

package com.green.acamatch.academyCost;

import com.green.acamatch.academyCost.model.GetAcademyCostInfoByCostId;
import com.green.acamatch.academyCost.model.GetAcademyCostInfoByMonth;
import com.green.acamatch.academyCost.model.GetSettlementListReq;
import com.green.acamatch.academyCost.model.GetSettlementListRes;
import com.green.acamatch.entity.academyCost.AcademyCost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademyCostService {
    private final AcademyCostMapper academyCostMapper;
    private final AcademyCostRepository academyCostRepository;

    public GetAcademyCostInfoByMonth getAcademyCostInfoByMonth() {
        return academyCostMapper.getAcademyCostInfo();
    }

    public List<GetSettlementListRes> getSettlementList(GetSettlementListReq req){
        return academyCostMapper.getSettlementList(req);
    }

    public GetAcademyCostInfoByCostId getAcademyCostInfoByCostId(long costId){
        return academyCostMapper.getAcademyCostInfoByCostId(costId);
    }

    public int updateStatus(Integer costId){
        AcademyCost academyCost = academyCostRepository.findById(costId).orElse(null);
        academyCost.setStatus(1);
        academyCostRepository.save(academyCost);
        return 1;
    }
}

package com.green.acamatch.academyCost;

import com.green.acamatch.academyCost.model.GetAcademyCostInfoByCostId;
import com.green.acamatch.academyCost.model.GetAcademyCostInfoByMonth;
import com.green.acamatch.academyCost.model.GetSettlementListReq;
import com.green.acamatch.academyCost.model.GetSettlementListRes;
import com.green.acamatch.entity.academyCost.AcademyCost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademyCostService {
    private final AcademyCostMapper academyCostMapper;
    private final AcademyCostRepository academyCostRepository;
    private final AcademyCostMessage academyCostMessage;

    public GetAcademyCostInfoByMonth getAcademyCostInfoByMonth() {
        return academyCostMapper.getAcademyCostInfo();
    }

    public List<GetSettlementListRes> getSettlementList(GetSettlementListReq req){
        return academyCostMapper.getSettlementList(req);
    }

    public GetAcademyCostInfoByCostId getAcademyCostInfoByCostId(long costId){
        return academyCostMapper.getAcademyCostInfoByCostId(costId);
    }

    public int updateStatus(Long costId) {
        LocalDate today = LocalDate.now();
        int lastDayOfMonth = today.lengthOfMonth(); // 해당 월의 마지막 날짜 가져오기

        if (today.getDayOfMonth() != lastDayOfMonth) {
            academyCostMessage.setMessage("정산 기간이 아닙니다.");
            return 0;
        }

        AcademyCost academyCost = academyCostRepository.findById(costId).orElse(null);

        if (academyCost == null) {
            academyCostMessage.setMessage("해당 정산 정보를 찾을 수 없습니다.");
            return 0;
        }

        academyCost.setStatus(1);
        academyCost.setUpdatedAt(LocalDateTime.now());
        academyCostRepository.save(academyCost);

        academyCostMessage.setMessage("정산이 완료되었습니다.");
        return 1;
    }

}

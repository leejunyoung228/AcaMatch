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
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public int updateStatuses(String costIds) {
        LocalDate today = LocalDate.now();
        YearMonth currentMonth = YearMonth.from(today); // 이번 달
        LocalDate firstDayOfThisMonth = currentMonth.atDay(1); // 이번 달 1일
        LocalDate firstDayOfNextMonth = currentMonth.plusMonths(1).atDay(1); // 다음 달 1일

        // 정산 가능 기간: 이번 달(1일~말일) 동안만 가능
        if (today.isBefore(firstDayOfThisMonth) || today.isAfter(firstDayOfNextMonth.minusDays(1))) {
            academyCostMessage.setMessage("정산 가능 기간이 아닙니다.");
            return 0;
        }

        // String을 List<Long>으로 변환
        List<Long> costIdList = Arrays.stream(costIds.split(","))
                .map(String::trim) // 공백 제거
                .map(Long::parseLong) // Long 타입 변환
                .collect(Collectors.toList());

        // 처리할 데이터가 없으면 반환
        if (costIdList.isEmpty()) {
            academyCostMessage.setMessage("정산할 내역이 없습니다.");
            return 0;
        }

        // 모든 costId 처리
        for (Long costId : costIdList) {
            AcademyCost academyCost = academyCostRepository.findById(costId).orElse(null);
            if (academyCost != null) {
                // 결제한 달 가져오기
                LocalDate createdAt = academyCost.getCreatedAt().toLocalDate();
                YearMonth paymentMonth = YearMonth.from(createdAt); // 결제한 월

                // 같은 달이면 정산 불가
                if (paymentMonth.equals(currentMonth)) {
                    academyCostMessage.setMessage("이번 달 결제 내역은 정산할 수 없습니다.");
                    return 0;
                }

                // 정산 가능하면 상태 업데이트
                academyCost.setStatus(1);
                academyCost.setUpdatedAt(LocalDateTime.now());
                academyCostRepository.save(academyCost);
            }
        }

        academyCostMessage.setMessage("정산이 완료되었습니다.");
        return 1;
    }
}

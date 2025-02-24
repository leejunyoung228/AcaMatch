package com.green.acamatch.academy.Service;

import com.green.acamatch.academy.PremiumRepository;
import com.green.acamatch.academy.model.JW.AcademyMessage;
import com.green.acamatch.academy.premium.model.PremiumDeleteReq;
import com.green.acamatch.academy.premium.model.PremiumPostReq;
import com.green.acamatch.entity.academy.PremiumAcademy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class PremiumService {
    private final AcademyMessage academyMessage;
    private final PremiumRepository premiumRepository;

    //프리미엄학원 신청(학원관계자)
    public int insPremium(PremiumPostReq req) {
        PremiumAcademy premiumAcademy = new PremiumAcademy();
        premiumAcademy.setAcaId(req.getAcaId());
        premiumAcademy.setStartDate(req.getStartDate());
        premiumAcademy.setEndDate(req.getEndDate());
        premiumAcademy.setPreCheck(req.getPreCheck());
        premiumAcademy.setPrice(req.getPrice());

        premiumRepository.save(premiumAcademy);
        academyMessage.setMessage("프리미엄학원을 신청하였습니다.");
        return 1;
    }

    //프리미엄학원 삭제(관리자)
    public int delPremium(PremiumDeleteReq req) {

    premiumRepository.deleteByAcaId(req.getAcaId());
    academyMessage.setMessage("프리미엄학원을 삭제하였습니다.");
    return 1;
    }

    //프리미엄 종료일이 지나면 삭제
    // 매일 자정에 실행되도록 설정 (주기적으로 실행)
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredPremiumAcademies() {
        LocalDate now = LocalDate.now();
        premiumRepository.deletePremiumAcademyByEndDate(now);
    }
}

package com.green.acamatch.academyCost;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PaymentCleanupScheduler {

    @Autowired
    private AcademyCostRepository academyCostRepository;

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void deleteExpiredPayments() {
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);

        int deletedRows = academyCostRepository.deleteExpiredPayments(fiveMinutesAgo);
        System.out.println(deletedRows + "개의 만료된 결제 데이터를 삭제했습니다.");
    }
}

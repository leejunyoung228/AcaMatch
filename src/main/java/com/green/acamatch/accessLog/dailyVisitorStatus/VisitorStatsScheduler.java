package com.green.acamatch.accessLog.dailyVisitorStatus;

import com.green.acamatch.entity.dailyVisitorStatus.MonthlyVisitorStat;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Component
@RequiredArgsConstructor
public class VisitorStatsScheduler {

    private final DailyVisitorStatService dailyVisitorStatService;
    private final DailyVisitorStatRepository dailyVisitorStatRepository;
    private final MonthlyVisitorStatRepository monthlyVisitorStatRepository;

    // 매월 1일 자정에 실행
    @Transactional
    @Scheduled(cron = "0 0 0 1 * *") // 매월 1일 자정 실행
    public void aggregateMonthlyStats() {
        System.out.println("📊 월간 방문자 통계 집계 시작...");

        YearMonth lastMonth = YearMonth.now().minusMonths(1);

        Long totalVisits = dailyVisitorStatRepository.countByVisitDateBetween(
                lastMonth.atDay(1), lastMonth.atEndOfMonth() // LocalDate 변환 후 조회
        );

        MonthlyVisitorStat monthlyStat = new MonthlyVisitorStat();
        monthlyStat.setMonth(lastMonth); // YearMonth 직접 저장 가능
        monthlyStat.setTotalVisits(totalVisits);

        monthlyVisitorStatRepository.save(monthlyStat);
        System.out.println("월간 방문자 통계 저장 완료: " + totalVisits + " 방문자");
    }

    // 30분마다 최근 방문자 수 집계
    @Scheduled(fixedRate = 1800000)
    public void aggregateVisitorStats() {
        long recentVisitors = dailyVisitorStatService.countRecentVisitors();
        System.out.println("최근 30분 방문자 수: " + recentVisitors);
    }

    // 매일 자정에 30일 이상 지난 방문 기록 삭제
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정 실행
    public void deleteOldVisitorStats() {
        System.out.println("🗑30일 이상 지난 방문 기록 삭제 시작...");
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusDays(30);
        dailyVisitorStatRepository.deleteByLastVisitBefore(oneMonthAgo);
        System.out.println("30일 이상 지난 방문 기록 삭제 완료.");
    }
}

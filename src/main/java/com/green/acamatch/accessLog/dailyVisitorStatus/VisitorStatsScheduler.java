package com.green.acamatch.accessLog.dailyVisitorStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VisitorStatsScheduler {

    private final DailyVisitorStatService dailyVisitorStatService;

    // 30분마다 실행 (1800초 = 30분)
    @Scheduled(fixedRate = 1800000)
    public void aggregateVisitorStats() {
        long recentVisitors = dailyVisitorStatService.countRecentVisitors();
        System.out.println("최근 30분 방문자 수: " + recentVisitors);
    }
}
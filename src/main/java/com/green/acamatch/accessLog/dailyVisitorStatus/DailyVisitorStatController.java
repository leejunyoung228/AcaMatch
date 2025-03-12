package com.green.acamatch.accessLog.dailyVisitorStatus;

import com.green.acamatch.entity.dailyVisitorStatus.MonthlyVisitorStat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("visitor")
@RequiredArgsConstructor
@Tag(name = "방문자 집계, 통계")
public class DailyVisitorStatController {

    private final DailyVisitorStatService dailyVisitorStatService;
    private final MonthlyVisitorStatRepository monthlyVisitorStatRepository;
    private final VisitorStatsScheduler visitorStatsScheduler;

    @Operation(summary = "방문자 추적", description = "사용자의 방문을 기록하고 방문 통계를 업데이트합니다.")
    @PostMapping("/track")
    public ResponseEntity<Map<String, Object>> trackVisitor(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        String ipAddress = request.getRemoteAddr();

        System.out.println("API 요청: visitor/track - Session ID: " + sessionId + ", IP: " + ipAddress);

        dailyVisitorStatService.saveOrUpdateVisitor(sessionId, ipAddress);

        // JSON 응답을 반환하도록 수정
        Map<String, Object> response = new HashMap<>();
        response.put("resultMessage", "Visitor tracked successfully");
        response.put("status", "success");

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "최근 방문자 수 조회", description = "최근 30분 동안 방문한 사용자 수를 반환합니다.")
    @GetMapping("/recent-visitors")
    public ResponseEntity<Long> getRecentVisitors() {
        long count = dailyVisitorStatService.countRecentVisitors();
        return ResponseEntity.ok(count);
    }

    // 특정 월의 방문자 통계 조회
    @Operation(summary = "(안 쓸 것 같음)월간 방문자 통계 조회", description = "특정 연-월(YYYY-MM)에 대한 방문자 통계를 조회합니다.")
    @GetMapping("/monthly-stats")
    public ResponseEntity<Map<String, Object>> getMonthlyStats(@RequestParam String yearMonth) {
        System.out.println("월간 방문자 통계 조회 요청: " + yearMonth);

        YearMonth targetMonth = YearMonth.parse(yearMonth);
        Optional<MonthlyVisitorStat> monthlyStat = monthlyVisitorStatRepository.findFirstByMonthOrderByIdDesc(targetMonth);

        if (monthlyStat.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("yearMonth", yearMonth);
            response.put("totalVisits", monthlyStat.get().getTotalVisits());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "(안 쓸 것 같음)월간 방문자 통계 강제 집계",
            description = "관리자용 API로, 강제로 월간 방문자 통계를 집계합니다.")
    @PostMapping("/test-aggregate-monthly")
    public ResponseEntity<String> testAggregateMonthly() {
        visitorStatsScheduler.aggregateMonthlyStats();
        return ResponseEntity.ok("월간 통계 강제 집계 완료!");
    }


    @Operation(summary = "대시보드 방문자 통계", description = "이번 주 & 저번 주 방문자 통계를 반환합니다. 0이면 이번 주, 1이면 저번 주")
    @GetMapping("/weekly-visitors")
    public ResponseEntity<Map<String, Object>> getWeeklyVisitors(@RequestParam(defaultValue = "0") int weeksAgo) {
        Map<String, Long> visitorStats = dailyVisitorStatService.countVisitorsForWeek(weeksAgo);

        Map<String, Object> response = new HashMap<>();
        response.put("weeklyVisitors", visitorStats);
        response.put("status", "success");

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "오늘 방문자 통계", description = "오늘 방문한 고유 IP 방문자 수를 반환합니다.")
    @GetMapping("/today-visitors")
    public ResponseEntity<Map<String, Object>> getTodayVisitors() {
        Map<String, Long> visitorStats = dailyVisitorStatService.countVisitorsForToday();

        Map<String, Object> response = new HashMap<>();
        response.put("todayVisitors", visitorStats);
        response.put("status", "success");

        return ResponseEntity.ok(response);
    }


    // 전체 누적 방문자 수 조회 API
    @Operation(summary = "누적 방문자 통계", description = "누적된  고유 IP 방문자 수를 반환합니다.")
    @GetMapping("/total-visitors")
    public ResponseEntity<Map<String, Object>> getTotalVisitors() {
        long totalVisitors = dailyVisitorStatService.getTotalVisitors();

        Map<String, Object> response = new HashMap<>();
        response.put("totalVisitors", totalVisitors);
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }
}

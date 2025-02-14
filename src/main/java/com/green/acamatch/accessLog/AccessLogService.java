package com.green.acamatch.accessLog;

import com.green.acamatch.entity.accesslog.AccessLog;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccessLogService {
    private final AccessLogRepository accessLogRepository;

    @Transactional
    public long getUniqueAccessCount() {
//        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(7);

        // 최근 하루 접속 기록 조회
//        List<AccessLog> logs = accessLogRepository.findByTimeStampAfter(oneDayAgo);
//
//        Set<String> uniqueIps = new HashSet<>();
//        for (AccessLog log : logs) {
//            uniqueIps.add(log.getIp()); // 고유 IP 목록을 저장
//        }
//
//        return uniqueIps.size(); // 고유 접속자 수 반환
        return accessLogRepository.count();
    }

    public Integer saveLog(HttpServletRequest request) {
        AccessLog accessLog = new AccessLog();
        accessLog.setIp(request.getRemoteAddr());
        accessLogRepository.save(accessLog);
        return 1;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteOldAccessLogs() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        accessLogRepository.deleteAccessLogByCreatedAtBefore(oneWeekAgo);
    }
}

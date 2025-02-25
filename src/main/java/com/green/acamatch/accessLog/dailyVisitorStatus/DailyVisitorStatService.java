package com.green.acamatch.accessLog.dailyVisitorStatus;

import com.green.acamatch.entity.dailyVisitorStatus.DailyVisitorStat;
import com.green.acamatch.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailyVisitorStatService {

    private final DailyVisitorStatRepository dailyVisitorStatRepository;

    @Transactional
    public void saveOrUpdateVisitor(String sessionId, String ipAddress, Long userId) {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        // 기존 방문자 조회
        Optional<DailyVisitorStat> existingRecord =
                dailyVisitorStatRepository.findByDateAndSessionIdAndIpAddress(today, sessionId, ipAddress);

        if (existingRecord.isPresent()) {
            // 기존 방문 기록이 있으면 visitCount 증가 및 마지막 방문 시간 업데이트
            DailyVisitorStat stat = existingRecord.get();
            stat.setVisitCount(stat.getVisitCount() + 1);
            stat.setLastVisit(now);
            dailyVisitorStatRepository.save(stat);
        } else {
            // 새로운 방문 기록 생성
            DailyVisitorStat newStat = new DailyVisitorStat();
            newStat.setDate(today);
            newStat.setSessionId(sessionId);
            newStat.setIpAddress(ipAddress);
            newStat.setVisitCount(1);
            newStat.setLastVisit(now);

            if (userId != null) {
                User user = new User();
                user.setUserId(userId);
                newStat.setUser(user);
            }

            dailyVisitorStatRepository.save(newStat);
        }
    }

    // 최근 30분 방문자 수 조회
    @Transactional(readOnly = true)
    public long countRecentVisitors() {
        LocalDateTime thirtyMinutesAgo = LocalDateTime.now().minusMinutes(30);
        return dailyVisitorStatRepository.findRecentVisitors(thirtyMinutesAgo).size();
    }
}
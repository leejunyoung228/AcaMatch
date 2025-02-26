package com.green.acamatch.accessLog.dailyVisitorStatus;

import com.green.acamatch.entity.dailyVisitorStatus.DailyVisitorStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyVisitorStatRepository extends JpaRepository<DailyVisitorStat, Long> {

    // 특정 날짜의 sessionId와 ipAddress 기준으로 방문 기록 찾기
    Optional<DailyVisitorStat> findByDateAndSessionIdAndIpAddress(LocalDate date, String sessionId, String ipAddress);

    // 최근 30분 내 방문 기록 조회
    @Query("SELECT d FROM DailyVisitorStat d WHERE d.lastVisit >= :timeThreshold")
    List<DailyVisitorStat> findRecentVisitors(@Param("timeThreshold") LocalDateTime timeThreshold);
}
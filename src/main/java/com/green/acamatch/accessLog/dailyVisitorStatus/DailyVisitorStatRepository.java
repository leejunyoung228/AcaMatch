package com.green.acamatch.accessLog.dailyVisitorStatus;


import com.green.acamatch.entity.dailyVisitorStatus.DailyVisitorStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

public interface DailyVisitorStatRepository extends JpaRepository<DailyVisitorStat, Long> {

//    @Query("SELECT d FROM DailyVisitorStat d " +
//            "WHERE d.visitDate = :date " +
//            "AND d.sessionId = :sessionId " +
//            "AND d.ipAddress = :ipAddress " +
//            "AND (d.user.userId = :userId OR :userId IS NULL)")
//    Optional<DailyVisitorStat> findByDateAndSessionIdAndIpAddressAndUser(
//            @Param("date") LocalDate date,
//            @Param("sessionId") String sessionId,
//            @Param("ipAddress") String ipAddress,
//            @Param("userId") Long userId
//    );

    @Query("SELECT d FROM DailyVisitorStat d WHERE d.lastVisit > :timeThreshold")
    List<DailyVisitorStat> findRecentVisitors(@Param("timeThreshold") LocalDateTime timeThreshold);

    // 30일 이상 지난 방문 기록 삭제
    @Transactional
    @Modifying
    @Query("DELETE FROM DailyVisitorStat d WHERE d.lastVisit < :oneMonthAgo")
    void deleteByLastVisitBefore(@Param("oneMonthAgo") LocalDateTime oneMonthAgo);

    // 특정 날짜 범위 내 방문자 수 조회
    Long countByVisitDateBetween(LocalDate startDate, LocalDate endDate);

    // JPQL로 직접 쿼리 작성하여 'user.userId'를 비교
    @Query("SELECT d FROM DailyVisitorStat d WHERE d.visitDate = :visitDate AND d.sessionId = :sessionId AND d.ipAddress = :ipAddress AND (:userId IS NULL OR d.user.userId = :userId)")
    Optional<DailyVisitorStat> findByVisitDateAndSessionIdAndIpAddressAndUser(
            @Param("visitDate") LocalDate visitDate,
            @Param("sessionId") String sessionId,
            @Param("ipAddress") String ipAddress,
            @Param("userId") Long userId
    );
}


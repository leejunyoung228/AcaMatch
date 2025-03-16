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

    @Query("SELECT COUNT(DISTINCT d.ipAddress) FROM DailyVisitorStat d WHERE d.visitDate BETWEEN :startDate AND :endDate")
    Long countDistinctByVisitDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // 오늘 방문한 고유 방문자 수 조회
    @Query("SELECT COUNT(DISTINCT d.ipAddress) FROM DailyVisitorStat d WHERE d.visitDate = :visitDate")
    long countDistinctByVisitDate(@Param("visitDate") LocalDate visitDate);


    // 로그인한 사용자의 방문 기록 조회 (userId 기준)
    @Query("SELECT d FROM DailyVisitorStat d WHERE d.visitDate = :visitDate AND d.user.userId = :userId")
    Optional<DailyVisitorStat> findByVisitDateAndUserId(@Param("visitDate") LocalDate visitDate, @Param("userId") Long userId);

    // 비로그인 사용자의 방문 기록 조회 (IP 기준)
    @Query("SELECT d FROM DailyVisitorStat d WHERE d.visitDate = :visitDate AND d.ipAddress = :ipAddress")
    Optional<DailyVisitorStat> findByVisitDateAndIpAddress(@Param("visitDate") LocalDate visitDate, @Param("ipAddress") String ipAddress);

    @Query("SELECT d FROM DailyVisitorStat d WHERE d.visitDate = :visitDate AND d.user.userId = :userId ORDER BY d.lastVisit DESC")
    List<DailyVisitorStat> findAllByVisitDateAndUserId(@Param("visitDate") LocalDate visitDate, @Param("userId") Long userId);

    @Query("SELECT d FROM DailyVisitorStat d WHERE d.visitDate = :visitDate AND d.ipAddress = :ipAddress ORDER BY d.lastVisit DESC")
    List<DailyVisitorStat> findAllByVisitDateAndIpAddress(@Param("visitDate") LocalDate visitDate, @Param("ipAddress") String ipAddress);

    @Query("SELECT COUNT(DISTINCT dvs.ipAddress) FROM DailyVisitorStat dvs WHERE dvs.visitDate = :today AND dvs.user IS NOT NULL")
    long countMemberVisitorsForToday(@Param("today") LocalDate today);

    @Query("SELECT COUNT(DISTINCT dvs.ipAddress) FROM DailyVisitorStat dvs WHERE dvs.visitDate = :today AND dvs.user IS NULL")
    long countNonMemberVisitorsForToday(@Param("today") LocalDate today);

    // 회원 방문자 수 (user_id가 NULL이 아닌 경우)
    @Query("SELECT COUNT(DISTINCT dvs.ipAddress) FROM DailyVisitorStat dvs " +
            "WHERE dvs.user IS NOT NULL AND dvs.visitDate BETWEEN :startDate AND :endDate")
    long countMemberVisitorsForWeek(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // 비회원 방문자 수 (user_id가 NULL인 경우)
    @Query("SELECT COUNT(DISTINCT dvs.ipAddress) FROM DailyVisitorStat dvs " +
            "WHERE dvs.user IS NULL AND dvs.visitDate BETWEEN :startDate AND :endDate")
    long countNonMemberVisitorsForWeek(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // 30분 내 방문을 제외한 "오늘의 방문자 수" 조회
    @Query("SELECT COUNT(DISTINCT d.ipAddress) FROM DailyVisitorStat d WHERE d.visitDate = :today AND d.lastVisit >= :timeLimit")
    long countTodayVisitors(@Param("today") LocalDate today, @Param("timeLimit") LocalDateTime timeLimit);

    // 회원 방문자 수 조회 (30분 내 재방문 제외)
    @Query("SELECT COUNT(DISTINCT d.ipAddress) FROM DailyVisitorStat d WHERE d.visitDate = :today AND d.user IS NOT NULL AND d.lastVisit >= :timeLimit")
    long countTodayMemberVisitors(@Param("today") LocalDate today, @Param("timeLimit") LocalDateTime timeLimit);

    // 비회원 방문자 수 조회 (30분 내 재방문 제외)
    @Query("SELECT COUNT(DISTINCT d.ipAddress) FROM DailyVisitorStat d WHERE d.visitDate = :today AND d.user IS NULL AND d.lastVisit >= :timeLimit")
    long countTodayGuestVisitors(@Param("today") LocalDate today, @Param("timeLimit") LocalDateTime timeLimit);

    // 누적 방문자 수 조회 (30분 단위 중복 제외)
    // 30분 단위 방문자 중복 제거하여 누적 방문자 수 조회
    @Query(value = "SELECT COUNT(*) FROM ( " +
            "SELECT DISTINCT d.ip_address, DATE_FORMAT(d.last_visit, '%Y-%m-%d %H:%i') " +
            "FROM daily_visitor_stat d) AS unique_visitors",
            nativeQuery = true)
    long countTotalVisitors();

}



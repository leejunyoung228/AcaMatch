package com.green.acamatch.accessLog.dailyVisitorStatus;

import com.green.acamatch.config.jwt.JwtUser;
import com.green.acamatch.entity.dailyVisitorStatus.DailyVisitorStat;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyVisitorStatService {

    private final DailyVisitorStatRepository dailyVisitorStatRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveOrUpdateVisitor(String sessionId, String ipAddress) {
        Long userId = getCurrentUserId();
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        Optional<DailyVisitorStat> existingRecord =
                dailyVisitorStatRepository.findByVisitDateAndSessionIdAndIpAddressAndUser(today, sessionId, ipAddress, userId);

        if (existingRecord.isPresent()) {
            DailyVisitorStat stat = existingRecord.get();
            LocalDateTime lastVisit = stat.getLastVisit();
            stat.setLastVisit(now);

            if (lastVisit.plusMinutes(30).isBefore(now)) {
                stat.setVisitCount(stat.getVisitCount() + 1);
            }

            dailyVisitorStatRepository.saveAndFlush(stat);
        } else {
            DailyVisitorStat newStat = new DailyVisitorStat();
            newStat.setVisitDate(today);
            newStat.setSessionId(sessionId);
            newStat.setIpAddress(ipAddress);
            newStat.setVisitCount(1);
            newStat.setLastVisit(now);

            if (userId != null) {
                User user = userRepository.findById(userId).orElse(null);

                if (user != null) {
                    user = userRepository.saveAndFlush(user);  // `merge()`를 사용
                }

                newStat.setUser(user);
            }

            dailyVisitorStatRepository.saveAndFlush(newStat);
        }
    }




    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            System.out.println("SecurityContextHolder에서 인증 정보를 가져오지 못함.");
            return null;
        }

        Object principal = auth.getPrincipal();
        System.out.println("Principal 정보: " + principal);

        if (principal instanceof JwtUser) {
            return ((JwtUser) principal).getSignedUserId();
        } else if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUserId();
        }

        return null;
    }

    @Transactional(readOnly = true)
    public long countRecentVisitors() {
        LocalDateTime thirtyMinutesAgo = LocalDateTime.now().minusMinutes(30);
        System.out.println("최근 방문자 조회 기준 시간: " + thirtyMinutesAgo);

        return dailyVisitorStatRepository.findRecentVisitors(thirtyMinutesAgo).size();
    }



    // 이번 주 & 저번 주 방문자 통계 조회
    public Map<String, Long> countVisitorsForWeek(int weeksAgo) {
        LocalDate startOfWeek = LocalDate.now().minusWeeks(weeksAgo).with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        long memberVisitors = dailyVisitorStatRepository.countMemberVisitorsForWeek(startOfWeek, endOfWeek);
        long nonMemberVisitors = dailyVisitorStatRepository.countNonMemberVisitorsForWeek(startOfWeek, endOfWeek);

        Map<String, Long> visitorStats = new HashMap<>();
        visitorStats.put("memberVisitors", memberVisitors);
        visitorStats.put("nonMemberVisitors", nonMemberVisitors);
        visitorStats.put("totalVisitors", memberVisitors + nonMemberVisitors);

        return visitorStats;
    }

    //오늘 방문자 수 조회 (고유 IP 기준)
    public Map<String, Long> countVisitorsForToday() {
        LocalDate today = LocalDate.now();

        long memberVisitors = dailyVisitorStatRepository.countMemberVisitorsForToday(today);
        long nonMemberVisitors = dailyVisitorStatRepository.countNonMemberVisitorsForToday(today);

        Map<String, Long> visitorStats = new HashMap<>();
        visitorStats.put("memberVisitors", memberVisitors);
        visitorStats.put("nonMemberVisitors", nonMemberVisitors);
        visitorStats.put("totalVisitors", memberVisitors + nonMemberVisitors); // 총 방문자 수도 반환

        return visitorStats;
    }

    // 오늘 방문자 수 조회 (30분 내 재방문 제외)
    @Transactional(readOnly = true)
    public long getTodayVisitors() {
        LocalDate today = LocalDate.now();
        LocalDateTime timeLimit = LocalDateTime.now().minusMinutes(30);
        return dailyVisitorStatRepository.countTodayVisitors(today, timeLimit);
    }

    // 오늘 회원 방문자 수 조회
    @Transactional(readOnly = true)
    public long getTodayMemberVisitors() {
        LocalDate today = LocalDate.now();
        LocalDateTime timeLimit = LocalDateTime.now().minusMinutes(30);
        return dailyVisitorStatRepository.countTodayMemberVisitors(today, timeLimit);
    }

    // 오늘 비회원 방문자 수 조회
    @Transactional(readOnly = true)
    public long getTodayGuestVisitors() {
        LocalDate today = LocalDate.now();
        LocalDateTime timeLimit = LocalDateTime.now().minusMinutes(30);
        return dailyVisitorStatRepository.countTodayGuestVisitors(today, timeLimit);
    }

    // 누적 방문자 수 조회 (30분 단위 중복 제외)
    @Transactional(readOnly = true)
    public Map<String, Long> getTotalVisitorStats() {
        LocalDate today = LocalDate.now();
        LocalDateTime timeLimit = LocalDateTime.now().minusMinutes(30);

        long totalVisitors = dailyVisitorStatRepository.countTotalVisitors();
        long todayVisitors = dailyVisitorStatRepository.countTodayVisitors(today, timeLimit);
        long todayMemberVisitors = dailyVisitorStatRepository.countTodayMemberVisitors(today, timeLimit);
        long todayGuestVisitors = dailyVisitorStatRepository.countTodayGuestVisitors(today, timeLimit);

        // JSON 응답 형식으로 반환
        Map<String, Long> visitorStats = new HashMap<>();
        visitorStats.put("totalVisitors", totalVisitors);
        visitorStats.put("todayVisitors", todayVisitors);
        visitorStats.put("todayMemberVisitors", todayMemberVisitors);
        visitorStats.put("todayGuestVisitors", todayGuestVisitors);

        return visitorStats;
    }


    @Transactional(readOnly = true)
    public long getTotalVisitors() {
        return dailyVisitorStatRepository.countTotalVisitors();
    }


}

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
        System.out.println("DailyVisitorStatService 실행됨! Session ID: " + sessionId + ", IP: " + ipAddress);

        Long userId = getCurrentUserId();
        System.out.println("로그인한 사용자 ID: " + userId);

        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        // 기존 방문 기록 조회
        Optional<DailyVisitorStat> existingRecord =
                dailyVisitorStatRepository.findByVisitDateAndSessionIdAndIpAddressAndUser(today, sessionId, ipAddress, userId);

        if (existingRecord.isPresent()) {
            DailyVisitorStat stat = existingRecord.get();
            LocalDateTime lastVisit = stat.getLastVisit(); // 기존 lastVisit 값 저장
            stat.setLastVisit(now);

            if (lastVisit.plusMinutes(30).isBefore(now)) {  // 기존 방문 시간과 현재 시간 비교
                stat.setVisitCount(stat.getVisitCount() + 1);
                System.out.println("방문 횟수 증가: " + stat.getVisitCount());
            } else {
                System.out.println("동일 방문자 - 30분 내 방문으로 방문 수 증가 안 함.");
            }
            dailyVisitorStatRepository.save(stat);
        } else {
            // 새로운 방문 기록 생성
            DailyVisitorStat newStat = new DailyVisitorStat();
            newStat.setVisitDate(today);
            newStat.setSessionId(sessionId);
            newStat.setIpAddress(ipAddress);
            newStat.setVisitCount(1);
            newStat.setLastVisit(now);

            // 여기서 User를 영속 상태로 변경 (해결 핵심 부분)
            if (userId != null) {
                User user = userRepository.findById(userId).orElse(null);

                if (user == null) {
                    user = new User();
                    user.setUserId(userId);
                    user = userRepository.save(user);  // DB에 저장 후 사용
                }

                newStat.setUser(user);  // 영속 상태의 User를 설정
            }

            dailyVisitorStatRepository.save(newStat);
            System.out.println("새로운 방문자 기록 저장 완료!");
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

    // 전체 누적 방문자 수 조회
    @Transactional(readOnly = true)
    public long getTotalVisitors() {
        return dailyVisitorStatRepository.countTotalVisitors();
    }


}

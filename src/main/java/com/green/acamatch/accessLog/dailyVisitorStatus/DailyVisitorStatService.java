package com.green.acamatch.accessLog.dailyVisitorStatus;

import com.green.acamatch.config.jwt.JwtUser;
import com.green.acamatch.entity.dailyVisitorStatus.DailyVisitorStat;
import com.green.acamatch.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailyVisitorStatService {

    private final DailyVisitorStatRepository dailyVisitorStatRepository;

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

            // userId가 존재하는 경우, 실제 User 객체 조회 후 설정 (userRepository 필요)
            if (userId != null) {
                User user = new User();
                user.setUserId(userId);
                newStat.setUser(user);
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


}

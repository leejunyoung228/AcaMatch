package com.green.acamatch.accessLog.dailyVisitorStatus;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/visitor")
@RequiredArgsConstructor
public class DailyVisitorStatController {

    private final DailyVisitorStatService dailyVisitorStatService;

    // 방문 기록 저장 API
    @PostMapping("/track")
    public ResponseEntity<String> trackVisitor(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        String ipAddress = request.getRemoteAddr();
        Long userId = getCurrentUserId();

        dailyVisitorStatService.saveOrUpdateVisitor(sessionId, ipAddress, userId);
        return ResponseEntity.ok("Visitor tracked successfully");
    }

    // 최근 30분 방문자 수 조회 API
    @GetMapping("/recent-visitors")
    public ResponseEntity<Long> getRecentVisitors() {
        long count = dailyVisitorStatService.countRecentVisitors();
        return ResponseEntity.ok(count);
    }

    private Long getCurrentUserId() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof CustomUserDetails)
                .map(principal -> ((CustomUserDetails) principal).getUserId())
                .orElse(null);
    }

}
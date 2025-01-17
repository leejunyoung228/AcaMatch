package com.green.studybridge.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
public class CookieUtils {
    public Cookie getCookie(HttpServletRequest request, String name) {
        return Arrays.stream(Optional.ofNullable(request.getCookies())
                        .orElseThrow(() -> new RuntimeException("Cookie not found")))
                .filter(item -> item.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cookie not found"));
    }

    public void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/api/user/access-token");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
}

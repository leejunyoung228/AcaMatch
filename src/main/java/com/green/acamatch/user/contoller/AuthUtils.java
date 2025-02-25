package com.green.acamatch.user.contoller;

import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.UserErrorCode;
import com.green.acamatch.config.jwt.JwtUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component // ✅ Spring Bean 등록
public class AuthUtils {

    /**
     * ✅ 현재 인증된 사용자(JWT User) 가져오기
     */
    public JwtUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            throw new CustomException(UserErrorCode.UNAUTHENTICATED);
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof JwtUser) {
            return (JwtUser) principal;
        } else {
            throw new CustomException(UserErrorCode.UNAUTHENTICATED);
        }
    }

    /**
     * ✅ 현재 로그인한 사용자의 ID 가져오기
     */
    public long getAuthenticatedUserId() {
        return getAuthenticatedUser().getSignedUserId();
    }
}

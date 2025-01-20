package com.green.studybridge.config.security;

import com.green.studybridge.config.jwt.JwtUser;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationFacade {
    public static long getSignedUserId() {
        return ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getSignedUserId();
    }
}

package com.green.acamatch.config.security;

import com.green.acamatch.config.jwt.JwtUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {
    public static long getSignedUserId() {
        return ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getSignedUserId();
    }

//    public long getUserId() {
//        return getSignedUserId();
//    }
}

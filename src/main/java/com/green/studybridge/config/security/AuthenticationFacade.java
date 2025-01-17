package com.green.studybridge.config.security;

import com.green.studybridge.config.jwt.JwtUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {
    public JwtUser getSignedUser() {
        return (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public long getSignedUserId() {
        return getSignedUser().getSignedUserId();
    }
}

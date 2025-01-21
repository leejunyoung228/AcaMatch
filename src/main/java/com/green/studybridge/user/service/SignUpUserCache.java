package com.green.studybridge.user.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.green.studybridge.config.exception.CustomException;
import com.green.studybridge.config.exception.EmailErrorCode;
import com.green.studybridge.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SignUpUserCache {
    private final Cache<String, User> tokenCache;
    private final Cache<String, Boolean> emailCache;

    public SignUpUserCache() {
        this.tokenCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).build();
        this.emailCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).build();
    }

    public void saveToken(String token, User user) {
        if (emailCache.getIfPresent(user.getEmail()) != null) {
            throw new CustomException(EmailErrorCode.EMAIL_ALREADY_SENT);
        }
        this.tokenCache.put(token, user);
        this.emailCache.put(user.getEmail(), Boolean.TRUE);
    }

    public User verifyToken(String token) {
        User user = Optional.ofNullable(this.tokenCache.getIfPresent(token)).orElseThrow(() ->
                new CustomException(EmailErrorCode.EXPIRED_OR_INVALID_TOKEN)
        );
        tokenCache.invalidate(token);
        emailCache.invalidate(user.getEmail());
        return user;
    }
}

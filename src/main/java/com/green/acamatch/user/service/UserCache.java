package com.green.acamatch.user.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.green.acamatch.config.constant.EmailConst;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.EmailErrorCode;
import com.green.acamatch.entity.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserCache {
    private final Cache<String, User> tokenCache;
    private final Cache<String, Boolean> emailCache;
    private final Cache<Long, String > pwCache;

    @Autowired
    public UserCache(EmailConst emailConst) {
        this.tokenCache = CacheBuilder.newBuilder().expireAfterWrite(emailConst.getExpiredTime(), TimeUnit.MINUTES).build();
        this.emailCache = CacheBuilder.newBuilder().expireAfterWrite(emailConst.getExpiredTime(), TimeUnit.MINUTES).build();
        this.pwCache = CacheBuilder.newBuilder().expireAfterWrite(emailConst.getExpiredTime(), TimeUnit.MINUTES).build();
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

    public String getTempPw(Long id) {
        return pwCache.getIfPresent(id);
    }

    public void saveTempPw(long id, String pw) {
        pwCache.put(id, pw);
    }
}

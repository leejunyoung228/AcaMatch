package com.green.studybridge.user;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.green.studybridge.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
            throw new RuntimeException("인증 메일이 이미 전송 되었습니다.");
        }
        this.tokenCache.put(token, user);
        this.emailCache.put(user.getEmail(), Boolean.TRUE);
    }

    public User verifyToken(String token) {
        User user = this.tokenCache.getIfPresent(token);
        if (user == null) {
            throw new RuntimeException("유효하지 않은 토큰 입니다");
        }
        tokenCache.invalidate(token);
        emailCache.invalidate(user.getEmail());
        return user;
    }
}

package com.green.studybridge.user;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SignUpCache {
    private final Cache<String, String > tokenCache;
    private final Cache<String, UserInfo> userCache;

    public SignUpCache() {
        this.tokenCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).build();
        this.userCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).build();
    }

    public void saveToken(String email, String token) {
        this.tokenCache.put(email, token);
    }

    public void saveUser(String token, UserInfo user) {
        this.userCache.put(token, user);
    }
}

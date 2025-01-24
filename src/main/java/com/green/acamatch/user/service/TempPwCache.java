package com.green.acamatch.user.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.green.acamatch.config.constant.EmailConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TempPwCache {
    private final Cache<Long, String > cache;

    @Autowired
    public TempPwCache(EmailConst emailConst) {
        this.cache = CacheBuilder.newBuilder().expireAfterWrite(emailConst.getExpiredTime(), TimeUnit.MINUTES).build();
    }

    public String get(Long id) {
        return cache.getIfPresent(id);
    }

    public void save(long id, String pw) {
        cache.put(id, pw);
    }
}

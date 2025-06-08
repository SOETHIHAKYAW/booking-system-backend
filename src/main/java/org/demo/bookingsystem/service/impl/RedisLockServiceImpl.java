package org.demo.bookingsystem.service.impl;

import org.demo.bookingsystem.service.RedisLockService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisLockServiceImpl implements RedisLockService {

    private final StringRedisTemplate redisTemplate;

    public RedisLockServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean acquireLock(String key, int expireSeconds) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, "locked", expireSeconds, TimeUnit.SECONDS);
        return success != null && success;
    }

    @Override
    public void releaseLock(String key) {
        redisTemplate.delete(key);
    }
}
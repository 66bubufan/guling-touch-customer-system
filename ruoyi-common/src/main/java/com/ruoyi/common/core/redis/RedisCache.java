package com.ruoyi.common.core.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisCache {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public <T> T getCacheObject(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    public void setCacheObject(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }
}

package com.greenkawsay.infrastructure.persistence;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisTest {

    private final StringRedisTemplate redisTemplate;

    public RedisTest(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void testConnection() {
        try {
            redisTemplate.opsForValue().set("test-key", "Hello Redis!");
            String value = redisTemplate.opsForValue().get("test-key");
            System.out.println("✅ Redis connection successful: " + value);
        } catch (Exception e) {
            System.out.println("❌ Redis connection failed: " + e.getMessage());
        }
    }
}
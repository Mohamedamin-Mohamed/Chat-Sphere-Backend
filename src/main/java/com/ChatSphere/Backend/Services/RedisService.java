package com.ChatSphere.Backend.Services;

import com.ChatSphere.Backend.Config.RedisConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {
    private final RedisConfig redisConfig;

    @Value("${redis.redisVerificationKey}")
    private String REDISKEY;


    public boolean addVerificationCodeToCache(String email, String code) {
        log.info("Adding verification code {} for {} to cache", email, code);
        try (Jedis jedis = redisConfig.connect()) {
            int CACHE_TTL_SECONDS = 600;
            String response = jedis.setex(REDISKEY + email, CACHE_TTL_SECONDS, code);
            return response.equals("OK");
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    public String getVerificationCodeFromCache(String email) {
        try (Jedis jedis = redisConfig.connect()) {
            String code = jedis.get(REDISKEY + email);
            return (code != null && !code.isEmpty()) ? code : null;
        }
    }
}

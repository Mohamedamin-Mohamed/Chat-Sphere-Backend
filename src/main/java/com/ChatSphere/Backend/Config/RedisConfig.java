package com.ChatSphere.Backend.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class RedisConfig {
    @Value("${redis.redisHost}")
    String REDIS_HOST;

    @Value("${redis.redisPassword}")
    String REDIS_PASSWORD;

    public Jedis connect() {
        Jedis jedis = new Jedis(REDIS_HOST, 6379, true);
        jedis.auth(REDIS_PASSWORD);
        return jedis;
    }

}

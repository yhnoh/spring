package org.example.keystore.config;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

public class KeyStoreRedisCacheManager extends RedisCacheManager {

    private final KeyStoreRedisCacheHandler keyStoreRedisCacheHandler;
    public KeyStoreRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, KeyStoreRedisCacheHandler keyStoreRedisCacheHandler) {
        super(cacheWriter, defaultCacheConfiguration);
        this.keyStoreRedisCacheHandler = keyStoreRedisCacheHandler;
    }

    //KeyStoreRedisCache 구현체 생성
    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfiguration) {
        return new KeyStoreRedisCache(name, getCacheWriter(), cacheConfiguration, keyStoreRedisCacheHandler);
    }
}

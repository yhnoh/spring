package org.example.keystore.config;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;

public class KeyStoreRedisCache extends RedisCache {

    private final KeyStoreRedisCacheHandler keyStoreRedisCacheHandler;

    protected KeyStoreRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfiguration, KeyStoreRedisCacheHandler keyStoreRedisCacheHandler) {
        super(name, cacheWriter, cacheConfiguration);
        this.keyStoreRedisCacheHandler = keyStoreRedisCacheHandler;
    }

    //캐시 저장
    @Override
    public void put(Object key, Object value) {
        super.put(key, value);
        Duration timeToLive = getCacheConfiguration().getTtlFunction().getTimeToLive(key, value);
        keyStoreRedisCacheHandler.put(super.getName(), (String) key, timeToLive);

    }

    //캐시 삭제
    @Override
    public void evict(Object key) {
        super.evict(key);
        keyStoreRedisCacheHandler.evict(super.getName(), (String) key);
    }
}

package org.example.keystore.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class KeyStoreRedisCacheHandler {

    private final RedisConnectionFactory redisConnectionFactory;
    private RedisTemplate<String, Map<String, Set<String>>> redisTemplate;

    @PostConstruct
    public void init() {
        redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        redisTemplate.afterPropertiesSet();
    }

    public void putKeyStore(String name, String keySuffix, Duration timeToLive){

        BoundHashOperations<String, String, Set<String>> hashOperations = redisTemplate.boundHashOps(name);
        hashOperations.expire(timeToLive);

        String[] arrKeySuffix = keySuffix.split("::");
        String hashKey = name + "::" + arrKeySuffix[0];

        Boolean hasKey = hashOperations.hasKey(hashKey);
        String value = name + "::" + keySuffix;

        Set<String> values;
        if(hasKey){
            values = hashOperations.get(hashKey);
            values.add(value);
        }else {
            values = new HashSet<>();
            values.add(value);
        }

        hashOperations.put(hashKey, values);
    }

    public void evictKeyStore(String name, String keySuffix){
        BoundHashOperations<String, String, Set<String>> hashOperations = redisTemplate.boundHashOps(name);

        String[] arrKeySuffix = keySuffix.split("::");
        String hashKey = name + "::" + arrKeySuffix[0];
        String value = name + "::" + keySuffix;
        Boolean hasKey = hashOperations.hasKey(hashKey);

        Set<String> values;
        if(hasKey){
            values = hashOperations.get(hashKey);
            values.remove(value);
            hashOperations.put(hashKey, values);
        }else {
            return;
        }

        if(values.isEmpty()){
            hashOperations.delete(hashKey);
        }

    }

}

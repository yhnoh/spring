package org.example.keystore.config;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class KeyStoreRedisCacheHandler {

    private final StringRedisTemplate redisTemplate;
    private static final String KEY_SUFFIX = "keys";
    private static final String KEY_DELIMITER = "::";

    /**
     *
     */
    public void put(String cacheKeyPrefix, String cacheKeySuffix, Duration timeToLive){

        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String key = getKey(cacheKeyPrefix, cacheKeySuffix);
                String value = getValue(cacheKeyPrefix, cacheKeySuffix);

                BoundSetOperations<String, String> setOperations = operations.boundSetOps(key);
                setOperations.expire(timeToLive);

                //키 조회시 없을 경우 추가
                if(!setOperations.isMember(value)){
                    setOperations.add(value);
                }

                return null;
            }
        });
    }


    /**
     *
     */
    public void evict(String cacheKeySuffix, String cacheKeyPrefix){

        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {

                String key = getKey(cacheKeySuffix, cacheKeyPrefix);
                String value = getValue(cacheKeySuffix, cacheKeyPrefix);


                BoundSetOperations<String, String> setOperations = operations.boundSetOps(key);
                //값을 통해 조회시 존재할 경우 제외
                if(setOperations.isMember(value)){
                    setOperations.remove(value);
                }
                return null;
            }
        });
    }

    public void evictAll(String key){
        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {

                BoundSetOperations<String, String> setOperations = operations.boundSetOps(key);
                Set<String> values = setOperations.members();

                if(values == null || values.isEmpty()){
                    return null;
                }

                //전체 값 삭제
                for (String value : values) {
                    BoundValueOperations valueOperations = operations.boundValueOps(value);
                    valueOperations.getAndDelete();
                    setOperations.remove(value);
                }

                return null;
            }
        });
    }

    private String getKey(String cacheKeyPrefix, String cacheKeySuffix) {
        String[] arrKeySuffix = cacheKeySuffix.split(KEY_DELIMITER);
        return cacheKeyPrefix + KEY_DELIMITER + arrKeySuffix[0] + KEY_DELIMITER + KEY_SUFFIX;
    }

    private String getValue(String cacheKeyPrefix, String cacheKeySuffix) {
        return cacheKeyPrefix + KEY_DELIMITER + cacheKeySuffix;
    }

}

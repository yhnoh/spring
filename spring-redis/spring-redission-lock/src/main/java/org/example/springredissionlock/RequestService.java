package org.example.springredissionlock;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService {

    private final StringRedisTemplate redisTemplate;


    public long request() {
        BoundValueOperations<String, String> operations = redisTemplate.boundValueOps("request");

        long requestCount = StringUtils.isEmpty(operations.get()) ? 0 : Long.parseLong(operations.get());
        log.info("요청 횟수 = {}", requestCount);
        if(requestCount >= 10) {
            log.info("요청 횟수 초과 = {}", requestCount);
            throw new RequestCountExceedException("요청 횟수 초과");
        }

        requestCount = operations.increment();
        boolean isPermanent = Objects.equals(operations.getExpire(), -1L);
        if(isPermanent) {
            operations.expire(10, TimeUnit.SECONDS);
        }

        return requestCount;
    }
}

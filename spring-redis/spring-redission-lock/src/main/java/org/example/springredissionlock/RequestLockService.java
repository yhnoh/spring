package org.example.springredissionlock;


import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestLockService {

    private final RequestService requestService;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;


    public long noLock() {
        return requestService.request();
    }


    public long lettuceLock(String lockName) {
        long requestCount;
        BoundValueOperations<String, String> operations = stringRedisTemplate.boundValueOps(lockName);
        try {
            // 값이 존재하지 않으면 값을 셋팅
            while(!operations.setIfAbsent("1", 10, TimeUnit.SECONDS)) {
                try {
                    Thread.sleep(50);
                } catch(InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            //비지니스 로직 수행
            requestCount = requestService.request();

        } finally {
            stringRedisTemplate.delete(lockName);
        }
        return requestCount;
    }


    public long redissonLock(String lockName) {
        long requestCount;
        RLock lock = redissonClient.getLock(lockName);
        try {
            /**
             * 락 획득과 동시에 키 생성
             * waitTime
             * 락을 획득할 수 없으면 10초간 wait
             * 10초 이후 락을 획득할 수 없으면 false
             *
             * leaseTime
             * 락 TTL
             */
            boolean lockable = lock.tryLock(10, 10, TimeUnit.SECONDS);

            if(!lockable) {
                log.info("Lock 획득 실패");

            }
            //비지니스 로직 수행
            requestCount = requestService.request();
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            /**
             * 락 해제와 동시에 키 삭제
             */
            lock.unlock();
        }

        return requestCount;
    }
}

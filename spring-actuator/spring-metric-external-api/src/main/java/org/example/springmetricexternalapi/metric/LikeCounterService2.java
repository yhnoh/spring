package org.example.springmetricexternalapi.metric;

import io.micrometer.core.annotation.Counted;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class LikeCounterService2 implements LikeService {

    private final AtomicInteger count = new AtomicInteger(100);

    @Counted("like.count")
    @Override
    public void like() {
        log.info("좋아요");
        count.incrementAndGet();
    }

    @Counted("like.count")
    @Override
    public void cancel() {
        log.info("좋아요 취소");
        count.decrementAndGet();
    }

    @Override
    public int count() {
        return count.get();
    }
}

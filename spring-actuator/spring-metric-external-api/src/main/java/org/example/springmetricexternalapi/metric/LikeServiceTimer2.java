package org.example.springmetricexternalapi.metric;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class LikeServiceTimer2 implements LikeService {

    private final AtomicInteger count = new AtomicInteger(100);
    private final MeterRegistry meterRegistry;

    public LikeServiceTimer2(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Timed("like.count")
    @Override
    public void like() {

        log.info("좋아요");
        count.incrementAndGet();
        sleep(1000);

    }

    @Timed("like.count")
    @Override
    public void cancel() {

        log.info("좋아요 취소");
        count.decrementAndGet();
        sleep(500);
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int count() {
        return count.get();
    }
}

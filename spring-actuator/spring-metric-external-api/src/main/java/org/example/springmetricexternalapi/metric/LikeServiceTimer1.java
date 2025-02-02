package org.example.springmetricexternalapi.metric;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class LikeServiceTimer1 implements LikeService {

    private final AtomicInteger count = new AtomicInteger(100);
    private final MeterRegistry meterRegistry;

    public LikeServiceTimer1(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void like() {

        Timer timer = Timer.builder("like.count")
                .description("좋아요")
                .tag("class", this.getClass().getName())
                .tag("method", "like")
                .register(meterRegistry);

        timer.record(() -> {
            log.info("좋아요");
            count.incrementAndGet();
            sleep(1000);
        });
    }

    @Override
    public void cancel() {

        Timer timer = Timer.builder("like.count")
                .description("좋아요")
                .tag("class", this.getClass().getName())
                .tag("method", "canceled")
                .register(meterRegistry);

        timer.record(() -> {
            log.info("좋아요 취소");
            count.decrementAndGet();
            sleep(500);
        });
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

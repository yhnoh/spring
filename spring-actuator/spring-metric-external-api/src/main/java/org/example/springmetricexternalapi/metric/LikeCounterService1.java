package org.example.springmetricexternalapi.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class LikeCounterService1 implements LikeService {

    private final AtomicInteger count = new AtomicInteger(100);
    private final MeterRegistry meterRegistry;

    public LikeCounterService1(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void like() {
        log.info("좋아요");
        count.incrementAndGet();

        Counter.builder("like.count")
                .description("좋아요 개수")
                .tag("class", this.getClass().getName())
                .tags("method", "like")
                .register(meterRegistry)
                .increment();
    }

    @Override
    public void cancel() {
        log.info("좋아요 취소");
        count.decrementAndGet();

        Counter.builder("like.count")
                .description("좋아요 개수")
                .tag("class", this.getClass().getName())
                .tags("method", "cancel")
                .register(meterRegistry)
                .increment();
    }

    @Override
    public int count() {
        return count.get();
    }
}

package org.example.springmetricexternalapi.metric;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class LikeService4 implements LikeService {


    private final AtomicInteger count = new AtomicInteger(100);
    private final MeterRegistry meterRegistry;

    public LikeService4(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void like() {
        log.info("좋아요");
        count.incrementAndGet();
    }

    @Override
    public void cancel() {
        log.info("좋아요 취소");
        count.decrementAndGet();
    }

    @Override
    public int count() {
        Gauge.builder("like.count", count, AtomicInteger::get)
                .description("좋아요 개수")
                .tag("class", this.getClass().getName())
                .tag("method", "count")
                .register(meterRegistry);
        return count.get();
    }
}


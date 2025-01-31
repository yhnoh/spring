package org.example.springmetricexternalapi.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;

import javax.management.timer.Timer;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class LikeService5 implements LikeService {

    private final AtomicInteger count = new AtomicInteger(100);
    private final MeterRegistry meterRegistry;

    public LikeService5(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void like() {
        log.info("좋아요");
        count.incrementAndGet();

        Timer.builder("like.count")
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
                .tags("method", "cancle")
                .register(meterRegistry)
                .increment();
    }

    @Override
    public int count() {
        return count.get();
    }
}

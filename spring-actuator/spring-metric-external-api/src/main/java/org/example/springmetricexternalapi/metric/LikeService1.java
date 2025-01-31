package org.example.springmetricexternalapi.metric;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class LikeService1 implements LikeService {

    private final AtomicInteger count = new AtomicInteger(100);

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
        return count.get();
    }
}

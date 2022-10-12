package com.example.springthreadpool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

@Slf4j
public class AsyncService {

    @Async
    public void async() throws InterruptedException {
        log.info("async start");
        Thread.sleep(1000);
        log.info("async end");
    }

    @Async
    public void asyncThrows() {
        log.info("async start");
        throw new RuntimeException("비동기 메소드 오류");
    }

    @Async
    public void asyncOverThread() throws InterruptedException {
        log.info("async start");
        Thread.sleep(1000);
        log.info("async end");

    }
}

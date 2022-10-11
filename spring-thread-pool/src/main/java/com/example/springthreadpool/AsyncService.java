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
    public void asyncThrows() throws InterruptedException {
        log.info("async start");
        Thread.sleep(1000);
        throw new RuntimeException("메시지 오류");
    }

}

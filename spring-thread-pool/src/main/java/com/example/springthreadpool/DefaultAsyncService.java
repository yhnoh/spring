package com.example.springthreadpool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

@Slf4j
public class DefaultAsyncService {
    @Async
    public void defaultAsync() throws InterruptedException {
        log.info("default async task start");
        Thread.sleep(1000);
        log.info("default async task end");
    }
}

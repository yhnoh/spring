package com.example.springthreadpool;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AsyncController {

    private final AsyncService asyncService;

    @GetMapping("/async")
    public String async() throws InterruptedException {
        asyncService.async();
        log.info("sync task complete");
        return "hello world";
    }

    @GetMapping("/async-throws")
    public String asyncThrows() throws InterruptedException {
        asyncService.asyncThrows();
        Thread.sleep(1000);
        log.info("sync task complete");
        return "hello world";
    }

    @GetMapping("/async-over-thread")
    public String asyncOverThread() throws InterruptedException {
        asyncService.asyncOverThread();
        log.info("sync task complete");
        return "hello world";
    }
}

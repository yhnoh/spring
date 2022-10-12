package com.example.springthreadpool;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DefaultAsyncController {

    private final DefaultAsyncService defaultAsyncService;

    @GetMapping("/default-async")
    public String defaultAsync() throws InterruptedException {
        defaultAsyncService.defaultAsync();
        log.info("default sync task complete");
        return "hello world";
    }
}

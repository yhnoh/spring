package com.example.springthreadpool;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TomcatThreadPoolController {

    @GetMapping
    public String hello() throws InterruptedException {
        log.info("start");
        Thread.sleep(3000);
        log.info("end");
        return "hello world";
    }

}

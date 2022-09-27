package com.example.springscheduler.threadpool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class SchedulerThreadService {

    private String now(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Scheduled(fixedRate = 1000)
    public void task1() throws InterruptedException {
        log.info("task1 now = {}", now());
        //task 1작업 5초동안 기다리기
        Thread.sleep(5000);
    }

    @Scheduled(fixedRate = 2000)
    public void task2(){
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("task2 now = {}", now);
    }
}

package com.example.springscheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@EnableScheduling
public class SchedulerService {

    private String now(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // fixedRate : 작업의 시작부터 시간을 카운트
    // 단위 : ms
    @Scheduled(fixedRate = 1000)
    public void fixedRateScheduler(){
        log.info("fixedRate now = {}", now());
    }

/*
    @Scheduled(fixedDelay = 1000)
    public void fixedDelayScheduler(){
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("fixedDelay now = {}", now);
    }

    // 초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    @Scheduled(cron = "2 * * * * *")
    public void cronScheduler(){
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("cron now = {}", now);
    }
*/
}



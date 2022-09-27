package com.example.springscheduler;

import com.example.springscheduler.async.SchedulerAsyncService;
import com.example.springscheduler.basic.SchedulerService;
import com.example.springscheduler.threadpool.SchedulerThreadService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerRegisterConfig {

    //@Bean
    public SchedulerService schedulerService(){
        return new SchedulerService();
    }

//    @Bean
    public SchedulerThreadService schedulerThreadService(){
        return new SchedulerThreadService();
    }

    @Bean
    public SchedulerAsyncService schedulerAsyncService(){
        return new SchedulerAsyncService();
    }
}

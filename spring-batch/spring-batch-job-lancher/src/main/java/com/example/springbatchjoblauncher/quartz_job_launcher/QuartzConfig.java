package com.example.springbatchjoblauncher.quartz_job_launcher;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail quartzJobDetail(){
        return JobBuilder.newJob(QuartzJobLauncher.class)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger quartzJobTrigger(){

        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(5).withRepeatCount(4);

        return TriggerBuilder.newTrigger()
                .forJob(quartzJobDetail())
                .withSchedule(scheduleBuilder)
                .build();
    }

}

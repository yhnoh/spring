package com.example.springbatchmonitoring;

import lombok.Value;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchMonitoringApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchMonitoringApplication.class, args);
    }

}

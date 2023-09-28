package com.example.springbatchmonitoring;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing()
public class SpringBatchMonitoringApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchMonitoringApplication.class, args);
    }

}

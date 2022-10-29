package com.example.springbatchdomain;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchDomainApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchDomainApplication.class, args);
    }

}

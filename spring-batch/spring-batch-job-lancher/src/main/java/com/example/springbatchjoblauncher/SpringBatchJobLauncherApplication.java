package com.example.springbatchjoblauncher;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class SpringBatchJobLauncherApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchJobLauncherApplication.class, args);
    }

}

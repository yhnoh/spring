package com.example.springbatchitemwriter;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class SpringBatchItemWriterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchItemWriterApplication.class, args);
    }

}

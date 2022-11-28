package com.example.springbatchitemreader;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class SpringBatchItemReaderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchItemReaderApplication.class, args);
    }

}

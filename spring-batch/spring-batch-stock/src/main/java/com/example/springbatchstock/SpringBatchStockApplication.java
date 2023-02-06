package com.example.springbatchstock;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class SpringBatchStockApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchStockApplication.class, args);
    }

}

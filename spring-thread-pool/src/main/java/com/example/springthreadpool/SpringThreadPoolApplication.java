package com.example.springthreadpool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class SpringThreadPoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringThreadPoolApplication.class, args);
    }

}

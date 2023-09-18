package com.example.springcloudstream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.util.function.Consumer;
import java.util.function.Supplier;

@SpringBootApplication
public class SpringCloudStreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudStreamApplication.class, args);
    }


//    @Bean
//    public Supplier<String> producer() {
//        return () -> "Hello from Supplier";
//    }
//    @Bean
//    public Consumer<String> consumer() {
//        return System.out::println;
//    }
}

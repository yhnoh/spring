package com.example.springthreadpool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@Configuration
@EnableAsync
public class DefaultAsyncConfig {

    @Bean
    public DefaultAsyncService defaultAsyncService(){
        return new DefaultAsyncService();
    }
}

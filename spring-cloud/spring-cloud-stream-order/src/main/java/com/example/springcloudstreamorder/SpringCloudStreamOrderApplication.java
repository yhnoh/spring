package com.example.springcloudstreamorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@EnableFeignClients
@SpringBootApplication
public class SpringCloudStreamOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudStreamOrderApplication.class, args);
    }

}

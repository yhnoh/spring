package com.example.springcloudstreamorder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class CloudStreamConfig {

    @Bean
    public Consumer<Long> goodsCacheEvict(){
        return aLong -> {
            System.out.println("goodsId = " + aLong);
        };
    }

}

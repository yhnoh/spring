package org.example.springmetricexternalapi.metric;

import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class LikeServiceConfig {

//    @Bean
//    public LikeService likeService() {
//        return new LikeService1();
//    }

//    @Bean
//    public LikeService likeService(MeterRegistry meterRegistry) {
//        return new LikeService2(meterRegistry);
//    }

    @Bean
    public LikeService likeService() {
        return new LikeService3();
    }

    @Bean
    public CountedAspect countedAspect(MeterRegistry meterRegistry) {
        return new CountedAspect(meterRegistry);
    }
}

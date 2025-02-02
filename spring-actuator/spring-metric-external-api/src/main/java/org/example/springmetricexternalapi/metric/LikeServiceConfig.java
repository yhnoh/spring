package org.example.springmetricexternalapi.metric;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class LikeServiceConfig {

/*
    @Bean
    public LikeService likeService() {
        return new LikeService1();
    }
*/

/*
    @Bean
    public LikeService likeService(MeterRegistry meterRegistry) {
        return new LikeCounterService1(meterRegistry);
    }
*/

/*
    @Bean
    public LikeService likeService() {
        return new LikeCounterService2();
    }

    @Bean
    public CountedAspect countedAspect(MeterRegistry meterRegistry) {
        return new CountedAspect(meterRegistry);
    }
*/

/*
    @Bean
    public LikeService likeService(MeterRegistry meterRegistry) {
        return new LikeGaugeService1(meterRegistry);
    }
*/

/*
    @Bean
    public LikeService likeService(MeterRegistry meterRegistry) {
        return new LikeServiceTimer1(meterRegistry);
    }
*/

/*
    @Bean
    public LikeService likeService(MeterRegistry meterRegistry) {
        return new LikeServiceTimer2(meterRegistry);
    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
*/

}

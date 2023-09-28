package com.example.springbatchmonitoring;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.PushGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class MonitoringConfig {

    /**
     * https://docs.spring.io/spring-batch/docs/current/reference/html/monitoring-and-metrics.html
     */

    private final String pushGatewayUrl = "localhost:9091";

    @Bean
    public PushGateway pushGateway(){
        PushGateway pushGateway = new PushGateway(pushGatewayUrl);

        return pushGateway;
    }

    @Bean
    public PrometheusMeterRegistry prometheusMeterRegistry(){
        PrometheusMeterRegistry prometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        Metrics.globalRegistry.add(prometheusMeterRegistry);
//        Metrics.globalRegistry.config().meterFilter(MeterFilter.denyNameStartsWith("spring.batch"));
        return prometheusMeterRegistry;
    }






}

package com.example.springbatchmonitoring;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.exporter.PushGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.Map;

//@EnableScheduling
//@Configuration
@Slf4j
public class MonitoringScheduler {

//    private final PushGateway pushGateway;
//    private final PrometheusMeterRegistry prometheusMeterRegistry;
//
//    private final String prometheusJobName = "springbatch";
//    private final String prometheusGroupingKey = "appname";
//    private final Map<String, String> groupingKey = Map.of(prometheusGroupingKey, prometheusJobName);
//
//    @Scheduled(fixedRateString = "1000")
//    public void scheduled(){
//        log.info("scheduled");
//    }
}

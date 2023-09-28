package com.example.springbatchmonitoring;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.exporter.PushGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class JobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private Random random = new Random();

    @Bean
    public Job taskletStepJob() {
        return jobBuilderFactory.get("taskletStepJob")
                .start(this.taskletStep1())
                .next(this.taskletStep2())
                .build();
    }

    @Bean
    public Step taskletStep1() {
        return stepBuilderFactory.get("taskletStep1")
                .tasklet((contribution, chunkContext) -> {
                    // simulate processing time
                    Thread.sleep(random.nextInt(3000));
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step taskletStep2() {
        return stepBuilderFactory.get("taskletStep2")
                .tasklet((contribution, chunkContext) -> {
                    int nextInt = random.nextInt(3000);
                    Thread.sleep(nextInt);
                    throw new Exception("Boom!");
//                    if (nextInt % 5 == 0) {
//
//                    }
//                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}

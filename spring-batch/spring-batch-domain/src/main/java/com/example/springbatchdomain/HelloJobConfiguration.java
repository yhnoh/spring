package com.example.springbatchdomain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class HelloJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job helloJob() {
        return jobBuilderFactory.get("helloJob")
                .start(startStep())
                .next(nextStep1())
                .next(nextStep2())
                .build();

    }


    @Bean
    public Step startStep() {
        return stepBuilderFactory.get("helloStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("firstStep");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step nextStep1() {
        return stepBuilderFactory.get("nextStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info("nextStep1");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step nextStep2() {
        return stepBuilderFactory.get("nextStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.info("nextStep2");
                    return RepeatStatus.FINISHED;
                }).build();
    }

}


package com.example.springbatchjoblauncher.rest_api_job_launcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class RestApiJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job restApiJob(){
        return jobBuilderFactory.get("restApiJob")
                .incrementer(new RunIdIncrementer())
                .start(restApiStep())
                .build();
    }

    @Bean
    public Step restApiStep() {
        return stepBuilderFactory.get("restApiStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("====> start restApiStep");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }


}

package com.example.springbatchexitcode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FailedJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean(name = "failedJob")
    public Job failedJob(){
        return jobBuilderFactory.get("failedJob")
                .start(this.failedStep())
                .build();
    }

    @Bean(name = "failedStep")
    public Step failedStep(){
        return stepBuilderFactory.get("failedStep")
                .tasklet(this.failedTasklet())
                .build();
    }

    @Bean(name = "failedTasklet")
    public Tasklet failedTasklet(){
        return (contribution, chunkContext) -> {
            throw new RuntimeException("실행 도중 에러 발생");
        };
    }
}

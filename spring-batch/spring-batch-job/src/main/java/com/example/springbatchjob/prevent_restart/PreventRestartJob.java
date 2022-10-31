package com.example.springbatchjob.prevent_restart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@Slf4j
@RequiredArgsConstructor
public class PreventRestartJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    //작업이 예상치 못하게 중단되었을때도 재시작 하지 않도록 설정
    //JobRestartException 예외를 던진다.
    @Bean
    public Job exceptionJob(){
        return this.jobBuilderFactory.get("exceptionJob")
                .preventRestart()
                .start(exceptionStep())
                .build();
    }

    @Bean
    public Step exceptionStep(){
        return this.stepBuilderFactory.get("startStep")
                .tasklet((contribution, chunkContext) -> {
                    throw new RuntimeException("exceptionStep");
                }).build();
    }
}

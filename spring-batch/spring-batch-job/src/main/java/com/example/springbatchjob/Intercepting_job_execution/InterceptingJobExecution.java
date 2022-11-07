package com.example.springbatchjob.Intercepting_job_execution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class InterceptingJobExecution {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * Job의 실행 전과 실행 후에 대한 인터셉터가 가능하다.
     */
    @Bean
    public Job interceptingJob(){
        return this.jobBuilderFactory.get("exceptionJob")
                .listener(new InterceptingJobExecutionListener())
                .incrementer(new RunIdIncrementer())
                .start(stepInInterceptingJob())
                .build();
    }

    @Bean
    public Step stepInInterceptingJob(){
        return this.stepBuilderFactory.get("startStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("startStep");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}

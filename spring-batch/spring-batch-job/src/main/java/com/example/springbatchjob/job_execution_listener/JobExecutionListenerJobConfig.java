package com.example.springbatchjob.job_execution_listener;

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

@Configuration
@Slf4j
@RequiredArgsConstructor
public class JobExecutionListenerJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * Job의 실행 전과 실행 후에 대한 인터셉터가 가능하다.
     */
    @Bean
    public Job jobExecutionListenerJob(){
        return this.jobBuilderFactory.get("jobExecutionListenerJob")
                .listener(new CustomJobExecutionListener())
                .incrementer(new RunIdIncrementer())
                .start(jobExecutionListenerStep())
                .build();
    }

    @Bean
    public Step jobExecutionListenerStep(){
        return this.stepBuilderFactory.get("jobExecutionListenerStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("jobExecutionListenerStep");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
